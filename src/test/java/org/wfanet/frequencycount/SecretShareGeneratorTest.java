// Copyright 2024 The Cross-Media Measurement Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.wfanet.frequencycount;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.wfanet.frequencycount.SecretShare;
import org.wfanet.frequencycount.SecretShareGeneratorRequest;

@RunWith(JUnit4.class)
public class SecretShareGeneratorTest {

  static {
    System.loadLibrary("secret_share_generator_adapter");
  }

  private static final int AES_KEY_LENGTH_IN_BYTES = 32;
  private static final int AES_IV_LENGTH_IN_BYTES = 16;

  @Test
  public void SecretShareGenerator_emptyFrequencyVectorShouldThrow() {
    SecretShareGeneratorRequest request = SecretShareGeneratorRequest.newBuilder()
        .setRingModulus(128).build();

    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> SecretShareGeneratorAdapter.SecretShareFrequencyVector(request.toByteArray()));
    assertThat(exception).hasMessageThat().contains("Input");
  }

  @Test
  public void SecretShareGenerator_invalidRingModulusShouldThrow() {
    SecretShareGeneratorRequest request = SecretShareGeneratorRequest.newBuilder()
        .addData(1).addData(2).addData(3).addData(4).addData(5)
        .setRingModulus(1).build();

    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> SecretShareGeneratorAdapter.SecretShareFrequencyVector(request.toByteArray()));
    assertThat(exception).hasMessageThat().contains("modulus");
  }

  @Test
  public void SecretShareGenerator_validSecretShareGeneratorRequest() throws Exception {
    SecretShareGeneratorRequest request = SecretShareGeneratorRequest.newBuilder()
        .addData(1).addData(2).addData(3).addData(4).addData(5)
        .setRingModulus(128).build();

    SecretShare secretShare = SecretShare.parseFrom(
        SecretShareGeneratorAdapter.SecretShareFrequencyVector(request.toByteArray()));

    System.out.println(secretShare.getShareVectorCount());
    assertThat(secretShare.getShareVectorCount()).isEqualTo(5);
    assertThat(secretShare.getShareSeed().getKey().size()).isEqualTo(AES_KEY_LENGTH_IN_BYTES);
    assertThat(secretShare.getShareSeed().getIv().size()).isEqualTo(AES_IV_LENGTH_IN_BYTES);
  }
}
