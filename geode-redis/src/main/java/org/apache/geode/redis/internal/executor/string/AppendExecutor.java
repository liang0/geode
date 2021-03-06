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
package org.apache.geode.redis.internal.executor.string;

import java.util.List;

import org.apache.geode.cache.Region;
import org.apache.geode.redis.internal.ByteArrayWrapper;
import org.apache.geode.redis.internal.Coder;
import org.apache.geode.redis.internal.Command;
import org.apache.geode.redis.internal.ExecutionHandlerContext;
import org.apache.geode.redis.internal.RedisConstants.ArityDef;

public class AppendExecutor extends StringExecutor {

  private final int VALUE_INDEX = 2;

  @Override
  public void executeCommand(Command command, ExecutionHandlerContext context) {
    List<byte[]> commandElems = command.getProcessedCommand();

    Region<ByteArrayWrapper, ByteArrayWrapper> r = context.getRegionProvider().getStringsRegion();

    if (commandElems.size() < 3) {
      command.setResponse(Coder.getErrorResponse(context.getByteBufAllocator(), ArityDef.APPEND));
      return;
    }

    ByteArrayWrapper key = command.getKey();
    checkAndSetDataType(key, context);
    ByteArrayWrapper string = r.get(key);

    byte[] stringByteArray = commandElems.get(VALUE_INDEX);
    if (string == null) {
      r.put(key, new ByteArrayWrapper(stringByteArray));
      command.setResponse(
          Coder.getIntegerResponse(context.getByteBufAllocator(), stringByteArray.length));
    } else {
      byte[] newValue = concatArrays(string.toBytes(), stringByteArray);
      string.setBytes(newValue);
      r.put(key, string);
      command.setResponse(Coder.getIntegerResponse(context.getByteBufAllocator(), newValue.length));
    }

  }

  private byte[] concatArrays(byte[] o, byte[] n) {
    int oLen = o.length;
    int nLen = n.length;
    byte[] combined = new byte[oLen + nLen];
    System.arraycopy(o, 0, combined, 0, oLen);
    System.arraycopy(n, 0, combined, oLen, nLen);
    return combined;
  }

}
