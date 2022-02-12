// Copyright 2022 The Cross-Media Measurement Authors
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

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VidSamplingBucketTesterTest {

  @Test
  public void testHashVidToUnitIntervalSpecificValues() throws IOException {
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();
    assertThat(vidTester.hashVidToUnitInterval(1L)).isWithin(0.001).of(0.7444);
    assertThat(vidTester.hashVidToUnitInterval(2L)).isWithin(0.001).of(0.5698);
    assertThat(vidTester.hashVidToUnitInterval(3L)).isWithin(0.001).of(0.5212);
  }

  @Test
  public void testHashVidToUnitIntervalConsistency() throws IOException {
    // Tests that if the same value is hashed at different times,
    // the same result is returned.
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();
    assertThat(vidTester.hashVidToUnitInterval(1L)).isWithin(0.001).of(0.7444);
    assertThat(vidTester.hashVidToUnitInterval(2L)).isWithin(0.001).of(0.5698);
    assertThat(vidTester.hashVidToUnitInterval(3L)).isWithin(0.001).of(0.5212);

    assertThat(vidTester.hashVidToUnitInterval(1L)).isWithin(0.001).of(0.7444);
    assertThat(vidTester.hashVidToUnitInterval(2L)).isWithin(0.001).of(0.5698);
    assertThat(vidTester.hashVidToUnitInterval(3L)).isWithin(0.001).of(0.5212);

    assertThat(vidTester.hashVidToUnitInterval(1L)).isWithin(0.001).of(0.7444);
    assertThat(vidTester.hashVidToUnitInterval(2L)).isWithin(0.001).of(0.5698);
    assertThat(vidTester.hashVidToUnitInterval(3L)).isWithin(0.001).of(0.5212);
  }

  @Test
  public void testHashVidToUnitIntervalValuesInRange() throws IOException {
    // Tests that values returned by VID hasher are between 0 and 1.
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();
    for (long i = 0L; i < 1000; i++) {
      double vid = vidTester.hashVidToUnitInterval(i);
      assertWithMessage("vid %s hashes to %s", i, vid).that((0.0 <= vid) && (vid <= 1.0)).isTrue();
    }
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
    assertThat(chi_square_statistic).isLessThan(16.91);
  }

  @Test
  public void testVidIsInSamplingBucket() throws IOException {
    VidSamplingBucketTester vidTester = new VidSamplingBucketTester();

    assertThat(vidTester.hashVidToUnitInterval(3L)).isWithin(0.001).of(0.5212f);
    assertThat(vidTester.vidIsInSamplingBucket(3L, 0.5f, 0.1f)).isTrue();
    assertThat(vidTester.vidIsInSamplingBucket(3L, 0.5f, 0.01f)).isFalse();
    assertThat(vidTester.vidIsInSamplingBucket(3L, 0.55f, 0.1f)).isFalse();
    assertThat(vidTester.vidIsInSamplingBucket(3L, 0.9f, 0.6f)).isFalse();
    assertThat(vidTester.vidIsInSamplingBucket(3L, 0.9f, 0.7f)).isTrue();
  }
}
