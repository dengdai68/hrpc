package com.hjk.rpc.registry.zookeeper;

import com.hjk.rpc.common.bean.ServiceObject;
import com.hjk.rpc.common.conf.ZookeeperConf;
import com.hjk.rpc.registry.registry.ServiceRegistry;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by hanjk on 16/9/7.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    private final ZkClient zkClient;

    private final ZookeeperConf zkconf;

    public ZookeeperServiceRegistry(ZookeeperConf zkconf) throws IOException {
        this.zkconf = zkconf;
        zkClient = new ZkClient(zkconf.getAddress(), zkconf.getSessionTimeout(), zkconf.getConnectionTimeout());
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
            zkClient.subscribeStateChanges(new ServerIZkStateListener(addressPath));
        }
        logger.debug("create addressPath node:{}",addressPath);
    }

    //服务节点监听,如果重连，重新创建服务节点
    class ServerIZkStateListener implements IZkStateListener{

        private String addressPath;

        public ServerIZkStateListener(String addressPath) {
            this.addressPath = addressPath;
        }
        @Override
        public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
            if(keeperState == Watcher.Event.KeeperState.SyncConnected){
                createServerNode();
            }
        }

        @Override
        public void handleNewSession() throws Exception {
            createServerNode();
        }

        private void createServerNode(){
            if(!zkClient.exists(addressPath)){
                zkClient.createEphemeral(addressPath);
                zkClient.subscribeStateChanges(new ServerIZkStateListener(addressPath));
            }
        }
    }
}
