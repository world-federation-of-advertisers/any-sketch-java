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

package org.wfanet.sampling;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;

/**
 * Filter VIDs by sampling bucket.
 *
 * <p>NOTE: IT IS IMPORTANT THAT THE SAME HASHING FUNCTION BE USED FOR ANY TWO SKETCHES THAT MIGHT
 * BE COMBINED BY THE SMPC, INCLUDING THE CASE WHEN THOSE SKETCHES ARE COMPUTED BY DIFFERENT EDPS.
 */
public final class VidSampler {

  /**
   * The mantissa of an IEEE 754 float is 23 bits.
   */
  private static final int Ieee754MantissaMask = 0x7f_ffff;

  /**
   * A divisor that is used to convert the lower 23 bits of the
   * fingerprinted value to a floating point value between 0 and 1.
   */
  private static final float maskDivisor = 1.0f / (float) Ieee754MantissaMask;

  /**
   * The hash function that will be used for this VidSampler.
   */
  private final HashFunction hashFunction;

  /**
   * A salt that is added to each VID prior to computing the fingerprint.
   * The purpose of this is to avoid accidentally using the same fingerprint
   * that is computed by {@link org.wfanet.anysketch.AnySketch}.  If the same
   * fingerprint is used by the sampler and by the sketch creation code,
   * then the only non-zero registers in the sketch will be registers that
   * correspond to values in the sampling interval.  Thus, cardinalities will
   * be underestimated.  This member is private because it is a requirement
   * that all EDPs use the same salt.
   */
  private static final long VID_SALT = 1414214L;

  /**
   * Constructs a new VidSampler.
   *
   * <p>Example usage: vidSampler = new VidSampler(Hashing.farmHashFingerprint64);
   *
   * @param hashFunction: The HashFunction that will be used for hashing VIDs. It is assumed that
   *     the hash function generates hashed values whose lower 23 bits are approximately uniformly
   *     distributed in the unit interval. Farmhash Fingerprint64 is a reasonable choice because it
   *     can be computed efficiently and the hash value for a given input is guaranteed to be the
   *     same across platforms.
   */
  public VidSampler(HashFunction hashFunction) {
    this.hashFunction = hashFunction;
  }

  /** Hashes a vid to a real number in the interval [0, 1]. */
  public float hashVidToUnitInterval(long vid) {
    Hasher vidHasher = hashFunction.newHasher();
    return maskDivisor * (float) (vidHasher.putLong(vid + VID_SALT).hash().asInt() & Ieee754MantissaMask);
  }

  /**
   * Returns true if the hashed VID is in the range from samplingIntervalStart to
   * samplingIntervalStart + samplingIntervalWidth, including wrap-around.
   *
   * @param vid The VID that is to be checked.
   * @param samplingIntervalStart The left endpoint of the VID sampling interval.
   * @param samplingIntervalEnd The right endpoint of the VID sampling interval.
   * @return True if the hashed VID is in the interval from samplingIntervalStart
   */
  public boolean vidIsInSamplingBucket(
      long vid, float samplingIntervalStart, float samplingIntervalWidth) {
    float hashedVid = hashVidToUnitInterval(vid);
    final float samplingIntervalEnd = samplingIntervalStart + samplingIntervalWidth;

    return ((samplingIntervalStart <= hashedVid && hashedVid < samplingIntervalEnd)
        || (hashedVid < samplingIntervalEnd - 1.0));
  }
}
