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

import java.util.Collection;

// Maintains a mapping between VIDs and FrequencyVector indexes
// for a particular PopulationSpec, and provides functions for
// sizing FrequencyVectors based on a sampling interval.
public class FrequencyVectorBuilder {

    // Create a FrequencyVectorBuilder
    public FrequencyVectorBuilder(VidIndex vidIndex, VidSamplingInterval samplingInterval) {
    }

    // TODO(kungfucraig): Update return type to FrequencyVector once Phi's PR is merged.
    public Long build() {
        return null;
    }

    // Add the given VID to the FrequencyVector.
    //
    // @throws IllegalArgumentException if the vid is not contained
    // by the Builder's sampling interval.
    public void addVid(long vid) {

    }

    // Adds each VID in the Collection to the FrequencyVector
    //
    // @throws IllegalArgumentException if any vid is not contained
    // by the Builder's sampling interval.
    public void addVids(Collection<Long> c) {
    }

    // Adds all VIDs from that builder to the FrequencyVector of this Builder.
    //
    // @throws IllegalArgumentException if the VidSamplingInterval of that
    // is not the same as the VidSamplingInterval of this.
    public void addVids(FrequencyVectorBuilder that) {
    }
}
