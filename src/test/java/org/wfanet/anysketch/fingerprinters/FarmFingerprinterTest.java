// Copyright 2020 The Cross-Media Measurement Authors
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

package org.wfanet.anysketch.fingerprinters;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FarmFingerprinterTest {
  static final String SALT = "salt";

  private final FarmFingerprinter fingerprinter = new FarmFingerprinter();
  private final FarmFingerprinter saltedFingerprinter = new FarmFingerprinter(SALT);

  @Test
  public void testFingerPrintWithNullValueFails() {
    assertThrows(NullPointerException.class, () -> fingerprinter.fingerprint(null));
  }

  @Test
  public void testTwoSameStringsMatchSucceeds() {
    String x = "Foo";
    String y = "Foo";
    assertThat(fingerprinter.fingerprint(x)).isEqualTo(fingerprinter.fingerprint(y));
    assertThat(saltedFingerprinter.fingerprint(x)).isEqualTo(saltedFingerprinter.fingerprint(y));

  }

  @Test
  public void testTwoDifferentStringsMatchFails() {
    String x = "Foo";
    String y = "Bar";
    assertThat(fingerprinter.fingerprint(x)).isNotEqualTo(fingerprinter.fingerprint(y));
    assertThat(saltedFingerprinter.fingerprint(x)).isNotEqualTo(saltedFingerprinter.fingerprint(y));
  }

  @Test
  public void testHashResultsMatchesExpected() {
    // These hard-coded inputs and outputs should match unit test in repo `common-cpp`.
    assertThat(fingerprinter.fingerprint("")).isEqualTo(Long.parseUnsignedLong("9ae16a3b2f90404f", 16));
    assertThat(fingerprinter.fingerprint("0")).isEqualTo(Long.parseUnsignedLong("D2ED96073B81823F", 16));
    assertThat(fingerprinter.fingerprint("12345")).isEqualTo(Long.parseUnsignedLong("E5B08D15925EBCF8", 16));
    assertThat(fingerprinter.fingerprint("vid")).isEqualTo(Long.parseUnsignedLong("43A6944721C22C7", 16));

    assertThat(saltedFingerprinter.fingerprint("")).isEqualTo(Long.parseUnsignedLong("3CAC4A31FEFB230B", 16));
    assertThat(saltedFingerprinter.fingerprint("0")).isEqualTo(Long.parseUnsignedLong("7A7B4D2365940F86", 16));
    assertThat(saltedFingerprinter.fingerprint("12345")).isEqualTo(Long.parseUnsignedLong("C61B65CC6C2C1E16", 16));
    assertThat(saltedFingerprinter.fingerprint("vid")).isEqualTo(Long.parseUnsignedLong("C3C55DC055B07504", 16));
  }
}
