/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.loaders.hbase;

import java.io.IOException;

import org.infinispan.loaders.BaseCacheStoreTest;
import org.infinispan.loaders.CacheStore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class HBaseCacheStoreTest extends BaseCacheStoreTest {
   private static EmbeddedServerHelper embedded;

   private static final boolean USE_EMBEDDED = true;

   /**
    * Set embedded hbase up and spawn it in a new thread.
    * 
    * @throws InterruptedException
    */
   @BeforeClass
   public static void setup() throws InterruptedException {
      if (USE_EMBEDDED) {
         embedded = new EmbeddedServerHelper();
         embedded.setup();
      }
   }

   @AfterClass(alwaysRun = true)
   public static void cleanup() throws IOException {
      if (USE_EMBEDDED) {
         EmbeddedServerHelper.teardown();
         embedded = null;
      }
   }

   @Override
   protected CacheStore createCacheStore() throws Exception {
      HBaseCacheStore cs = new HBaseCacheStore();
      // This uses the default config settings in HBaseCacheStoreConfig
      HBaseCacheStoreConfig conf = new HBaseCacheStoreConfig();

      if (USE_EMBEDDED) {
         // overwrite the ZooKeeper client port with the port from the embedded server
         conf.setHbaseZookeeperPropertyClientPort(Integer
                  .toString(EmbeddedServerHelper.zooKeeperPort));
      }

      cs.init(conf, getCache(), getMarshaller());
      cs.start();
      return cs;
   }

}