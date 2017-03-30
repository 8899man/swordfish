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
package com.baifendian.swordfish.common.datasource.hbase;

import com.baifendian.swordfish.dao.enums.DbType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class HBaseHandlerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testIsConnectable() throws IOException {
    String paramStr = "{ \"zkQuorum\": \"bgsbtsp0006-dqf,bgsbtsp0007-dqf,bgsbtsp0008-dqf\", \"zkZnodeParent\": \"/hbase\" }\n";
    HBaseHandler handler = new HBaseHandler(DbType.HBASE11X, paramStr);
    handler.isConnectable();
  }
}