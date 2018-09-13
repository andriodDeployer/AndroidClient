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

package serialization;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * jupiter
 * org.jupiter.serialization.hessian.io
 *
 * @author jiachun.fjc
 */
public final class Outputs {

    public static ObjectOutputStream getOutput(OutputBuf outputBuf) throws IOException {
        return new ObjectOutputStream(outputBuf.outputStream());
    }

    public static ObjectOutputStream getOutput(OutputStream buf) throws IOException {
        return new ObjectOutputStream(buf);
    }

    private Outputs() {}
}