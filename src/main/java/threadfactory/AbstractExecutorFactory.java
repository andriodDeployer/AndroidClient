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


import java.util.concurrent.ThreadFactory;

/**
 * jupiter
 * org.jupiter.rpc.executor
 *
 * @author jiachun.fjc
 */
public abstract class AbstractExecutorFactory implements ExecutorFactory {

    protected ThreadFactory threadFactory(String name) {
     //   boolean affinity = SystemPropertyUtil.getBoolean(EXECUTOR_AFFINITY_THREAD, false);
//        if (affinity) {
//            return new AffinityNamedThreadFactory(name);
//        } else {
//            return new NamedThreadFactory(name);
//        }
        return  new NamedThreadFactory(name);
    }

    protected int coreWorkers(Target target) {

        return Runtime.getRuntime().availableProcessors()<< 1;

    }

    protected int maxWorkers(Target target) {
        return 32;
    }

    protected int queueCapacity(Target target) {
       return 32768;
    }
}
