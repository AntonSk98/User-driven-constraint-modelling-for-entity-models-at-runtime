/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development.exception.instance;

/**
 * Concrete {@link InstanceException} thrown in case of instance-related operation failures.
 */
public class InstanceOperationException extends InstanceException {
    public InstanceOperationException(String message) {
        super(message);
    }

    public InstanceOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceOperationException(Throwable cause) {
        super(cause);
    }
}
