/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.geode.redis;


import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testcontainers.containers.GenericContainer;
import redis.clients.jedis.Jedis;

import org.apache.geode.test.junit.categories.RedisTest;

@Category({RedisTest.class})
public class RenameDockerAcceptanceTest extends RenameIntegrationTest {

  private static GenericContainer redisContainer;

  @BeforeClass
  public static void setUp() {
    rand = new Random();
    redisContainer = new GenericContainer<>("redis:5.0.6").withExposedPorts(6379);
    redisContainer.start();
    jedis = new Jedis("localhost", redisContainer.getFirstMappedPort(), REDIS_CLIENT_TIMEOUT);
  }

  @AfterClass
  public static void tearDown() {
    jedis.close();
  }

  public int getPort() {
    return redisContainer.getFirstMappedPort();
  }

  @Test
  public void testSortedSet() {
    // TODO: GEODE-7910 Update RENAME command in Geode Redis to match native Redis
  }

  @Test
  public void testList() {
    // TODO: GEODE-7910 Update RENAME command in Geode Redis to match native Redis
  }

  @Test
  @Ignore("Test only applies to Geode Redis, ignored for native Redis")
  public void testProtectedString() {}
}
