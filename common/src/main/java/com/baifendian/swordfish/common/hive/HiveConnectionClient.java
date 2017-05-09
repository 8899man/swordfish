/*
 * Copyright (C) 2017 Baifendian Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baifendian.swordfish.common.hive;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.hive.jdbc.HiveConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiveConnectionClient {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private GenericKeyedObjectPool pool;

  /**
   * 超时时间，单位为 ms，默认为 3s
   */
  private int timeout = 3000;

  /**
   * 最大活跃连接数
   */
  private int maxActive = 1024;

  /**
   * 链接池中最大空闲的连接数, 默认为 100
   */
  private int maxIdle = 100;

  /**
   * 连接池中最少空闲的连接数, 默认为 0
   */
  private int minIdle = 0;

  /**
   * 当连接池资源耗尽时，调用者最大阻塞的时间
   */
  private int maxWait = 2000;

  /**
   * 空闲链接检测线程，检测的周期，毫秒数，默认为 3 min，-1 表示关闭空闲检测
   */
  private int timeBetweenEvictionRunsMillis = 180000;

  /**
   * 空闲时是否进行连接有效性验证，如果验证失败则移除，默认为 true
   */
  private boolean testWhileIdle = true;

  /**
   * hive 的连接客户端
   */
  private static HiveConnectionClient hiveConnectionClient;

  private HiveConnectionClient() {
    try {
      pool = bulidClientPool();
    } catch (Exception e) {
      logger.error("build client pool exception", e);
    }
  }

  /**
   * 构建单例, 初始化 hive 连接的客户端
   *
   * @return
   */
  public static HiveConnectionClient getInstance() {
    if (hiveConnectionClient == null) {
      synchronized (HiveConnectionClient.class) {
        if (hiveConnectionClient == null) {
          hiveConnectionClient = new HiveConnectionClient();
        }
      }
    }

    return hiveConnectionClient;
  }

  /**
   * 构建 hive 客户端的连接池
   *
   * @return
   */
  protected GenericKeyedObjectPool bulidClientPool() {
    GenericKeyedObjectPool.Config poolConfig = new GenericKeyedObjectPool.Config();

    poolConfig.maxActive = maxActive;
    poolConfig.maxIdle = maxIdle;
    poolConfig.minIdle = minIdle;
    poolConfig.maxWait = maxWait;
    poolConfig.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    poolConfig.testWhileIdle = testWhileIdle;

    HiveConnectionPoolFactory clientFactory = new HiveConnectionPoolFactory();

    return new GenericKeyedObjectPool(clientFactory, poolConfig);
  }

  /**
   * 从连接池获取一个具体的 hive 连接
   *
   * @param connectionInfo
   * @return
   * @throws Exception
   */
  public HiveConnection borrowClient(ConnectionInfo connectionInfo) throws Exception {
    return (HiveConnection) pool.borrowObject(connectionInfo);
  }

  /**
   * 返回一个 hive 连接对象
   *
   * @param connectionInfo
   * @param client
   */
  public void returnClient(ConnectionInfo connectionInfo, HiveConnection client) {
    if (client != null) {
      try {
        pool.returnObject(connectionInfo, client);
      } catch (Exception e) {
        logger.warn("HiveConnectionClient returnClient exception", e);
      }
    }
  }

  /**
   * 校验连接信息是否合法
   *
   * @param connectionInfo
   * @param client
   */
  public void invalidateObject(ConnectionInfo connectionInfo, HiveConnection client) {
    try {
      pool.invalidateObject(connectionInfo, client);
    } catch (Exception e) {
      logger.error("HiveConnectionClient invalidateObject error", e);
    }
  }

  /**
   * 清空连接池
   */
  public void clear() {
    pool.clear();
  }
}
