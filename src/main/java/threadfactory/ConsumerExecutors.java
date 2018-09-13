/*
 * Copyright (c) 2015 The Jupiter Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package threadfactory;


import io.netty.util.internal.SystemPropertyUtil;
import processor.CloseableExecutor;

/**
 * jupiter
 * org.jupiter.rpc.consumer.processor
 *
 * @author jiachun.fjc
 */
public class ConsumerExecutors {

 //   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ConsumerExecutors.class);

    private static final CloseableExecutor executor;

    static {
        String factoryName = SystemPropertyUtil.get("jupiter.executor.factory.consumer.factory_name", "callerRuns");
        ExecutorFactory factory;

        factory = new ThreadPoolExecutorFactory();

        executor = factory.newExecutor(ExecutorFactory.Target.CONSUMER, "jupiter-consumer-processor");
    }

    public static CloseableExecutor executor() {
        return executor;
    }

    public static void execute(Runnable r) {
        executor.execute(r);
    }
}
