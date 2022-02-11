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

package org.wfanet.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VidSamplingBucketTesterTest {

  @Test
  public void testHashVidToUnitIntervalSpecificValues() throws IOException {
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();
    assertEquals(vidTester.hashVidToUnitInterval(1L), 0.7444, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(2L), 0.5698, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(3L), 0.5212, 0.001);
  }

  @Test
  public void testHashVidToUnitIntervalConsistency() throws IOException {
    // Tests that if the same value is hashed at different times,
    // the same result is returned.
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();
    assertEquals(vidTester.hashVidToUnitInterval(1L), 0.7444, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(2L), 0.5698, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(3L), 0.5212, 0.001);

    assertEquals(vidTester.hashVidToUnitInterval(1L), 0.7444, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(2L), 0.5698, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(3L), 0.5212, 0.001);

    assertEquals(vidTester.hashVidToUnitInterval(1L), 0.7444, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(2L), 0.5698, 0.001);
    assertEquals(vidTester.hashVidToUnitInterval(3L), 0.5212, 0.001);
  }

  @Test
  public void testHashVidToUnitIntervalChiSquaredDistribution() throws IOException {
    // Tests that when a large number of samples are drawn, the distribution
    // passes the chi-squared goodness of fit test.
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();
    final int NSAMPLES = 1000;
    int[] buckets = new int[10];
    for (long i = 0L; i < NSAMPLES; i++) {
      double vid = vidTester.hashVidToUnitInterval(i);
      int bucket_id = (int) (vid * buckets.length);
      buckets[bucket_id]++;
    }

    double chi_square_statistic = 0.0;
    double expected = (double) NSAMPLES / (double) buckets.length;
    for (int i = 0; i < buckets.length; i++) {
      double error = buckets[i] - expected;
      chi_square_statistic += error * error / expected;
    }

    // 16.91 is the 95th percentile of the chi-squared distribution with
    // 9 degrees of freedom.
    assertTrue(chi_square_statistic < 16.91);
  }

  @Test
  public void testVidIsInSamplingBucket() throws IOException {
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();

    assertEquals(vidTester.hashVidToUnitInterval(3L), 0.5212f, 0.001f);
    assertTrue(vidTester.vidIsInSamplingBucket(3L, 0.5f, 0.1f));
    assertFalse(vidTester.vidIsInSamplingBucket(3L, 0.5f, 0.01f));
    assertFalse(vidTester.vidIsInSamplingBucket(3L, 0.55f, 0.1f));
    assertFalse(vidTester.vidIsInSamplingBucket(3L, 0.9f, 0.6f));
    assertTrue(vidTester.vidIsInSamplingBucket(3L, 0.9f, 0.7f));
  }
}
