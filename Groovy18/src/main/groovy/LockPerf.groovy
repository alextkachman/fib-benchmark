/*
* Copyright 2009-2011 MBTE Sweden AB.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

class LockPerf {
    public static void main(String[] args) {
        def processors = Runtime.runtime.availableProcessors()
        for(def threadNum = 1; threadNum <= 1024; threadNum = threadNum < 2*processors ? threadNum+1 : threadNum*2) {
          def counter = new AtomicInteger ()
          def cdl = new CountDownLatch(threadNum)

          def lock = new ReentrantLock()

          def start = System.currentTimeMillis()
          for(i in 0..<threadNum) {
              Thread.start {
                for(;;) {
                  lock.lock()
                  try {
                    if(counter.get() == 100000000) {
                      cdl.countDown()
                      break
                    }
                    else {
                      counter.incrementAndGet()
                    }
                  }
                  finally {
                    lock.unlock()
                  }
                }
              }
          }

          cdl.await()
          println "$threadNum ${System.currentTimeMillis() - start}"
        }
    }
}