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

package org.wfanet.hmss;

import org.wfanet.measurement.api.v2alpha.PopulationSpec;
import org.wfanet.measurement.api.v2alpha.MeasurementSpec.VidSamplingInterval;

// Maintains a mapping between VIDs and FrequencyVector indexes
// for a particular PopulationSpec, and provides functions for
// sizing FrequencyVectors based on a sampling interval.
public class VidIndex {

    // Describe a range of a FrequencyVector spanned by a VidSamplingInterval.
    public static class FrequencyVectorRange {
        // The start of a range of indexes in a FrequencyVector
        FrequencyVectorRange(int start, int end_inclusive) {
            this.start = start;
            this.end_inclusive = end_inclusive;
        }
        public final int start;
        public final int end_inclusive;
    }

    // Create a VID Index from a Population Spec.
    public VidIndex(PopulationSpec populationSpec) {
    }

    // Get the index in the Frequency vector for the given VID.
    public int getFrequencyVectorIndex(long vid) {
        return 0;
    }

    public FrequencyVectorRange getFrequencyVectorRange(VidSamplingInterval samplingInterval) {
        return null;
    }
}
