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

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * Filter VIDs by sampling bucket.
 *
 * <p>NOTE: IT IS IMPORTANT THAT ALL EDPS USE THE SAME METHOD FOR HASHING VIDS TO THE UNIT INTERVAL.
 * DO NOT CHANGE OR MODIFY THE WAY THAT HASH VALUES ARE COMPUTED.
 */
public class VidSamplingBucketTester {

  // The mantissa of an IEEE 754 double is 52 bits.
  private final long Ieee754MantissaMask = 0xf_ffff_ffff_ffffL;

  // A divisor that is used to convert the lower 52 bits of the
  // fingerprinted value to a floating point value between 0 and 1.
  private final double maskDivisor = 1.0 / (double) Ieee754MantissaMask;

  /** Hashes a vid to a real number in the interval [0, 1]. */
  public double hashVidToUnitInterval(long vid) {
    // FarmHash64 seems to be a reasonable choice for a hash function
    // because it is guaranteed to be the same across platforms and it
    // is relatively efficient to compute.  Note that it is not
    // cryptographically secure.
    Hasher vidHasher = Hashing.farmHashFingerprint64().newHasher();

    return maskDivisor * (double) (vidHasher.putLong(vid).hash().asLong() & Ieee754MantissaMask);
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
    Double hashedVid = hashVidToUnitInterval(vid);

    return ((samplingIntervalStart <= hashedVid
            && hashedVid < samplingIntervalStart + samplingIntervalWidth)
        || (hashedVid < samplingIntervalStart + samplingIntervalWidth - 1.0));
  }
}
