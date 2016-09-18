package com.hjk.rpc.registry.zookeeper;

import java.io.IOException;
import java.util.Vector;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.conf.ZookeeperConf;
import com.hjk.rpc.registry.StateListener;
import com.hjk.rpc.registry.registry.ServiceRegistry;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    private final ZkClient zkClient;

    private final ZookeeperConf zkconf;

    private Vector<StateListener> stateListeners = new Vector<StateListener>();

    public ZookeeperServiceRegistry(ZookeeperConf zkconf) throws IOException {
        this.zkconf = zkconf;
        zkClient = new ZkClient(zkconf.getAddress(), zkconf.getSessionTimeout(), zkconf.getConnectionTimeout());
        zkClient.subscribeStateChanges(new ServerIZkStateListener());
    }

    @Override
    public void register(ServiceObject service){
        service.validate();
        String servicePath = zkconf.getRegistryPath() +
                "/" + service.getAppServer() +
                "/" + service.getServiceName() ;
        if(!zkClient.exists(servicePath)){
            zkClient.createPersistent(servicePath,true);
        }
        //创建service address节点
        String addressPath = servicePath + "/" + service.getServiceAddress();
        if(!zkClient.exists(addressPath)){
            zkClient.createEphemeral(addressPath);
        }
        logger.debug("create addressPath node:{}",addressPath);
    }

    @Override
    public void addStateListener(StateListener stateListener){
        stateListeners.add(stateListener);
    }

    @Override
    public void notifyListener(int stat){
        for (StateListener s:stateListeners){
            s.stateChanged(stat);
        }
    }

    //服务节点监听,如果重连，重新创建服务节点
    class ServerIZkStateListener implements IZkStateListener{

        @Override
        public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
            if (state == Watcher.Event.KeeperState.Disconnected) {
                notifyListener(StateListener.DISCONNECTED);
            } else if (state == Watcher.Event.KeeperState.SyncConnected) {
                notifyListener(StateListener.CONNECTED);
            }
        }

        @Override
        public void handleNewSession() throws Exception {
            notifyListener(StateListener.RECONNECTED);
        }
    }
}
