// Copyright 2021 The Cross-Media Measurement Authors
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

// DO NOT SUBMIT
// Brief illustration of how these libraries would work.
class Main {
  static void main(String[] args) {
    // Build the map
    PopulationSpec population;
    VidIndex index = new VidIndex(population);

    VidSamplingInterval samplingInterval = requisition.getSamplingInterval();
    FrequencyVectorBuilder builder = new FrequencyVectorBuilder(index, samplingInterval);

    Collection<Long> vids = getVids(samplingInterval);
    for (Long vid : vids) {
      builder.add(vid);
    }

    FrequencyVectorBuilder anotherBuilder = new FrequencyVectorBuilder(index, samplingInterval);
    Collection<Long> moreVids = getMoreVids(samplingInterval);
    anotherBuilder.add(moreVids);

    builder.add(anotherBuilder);

    FrequencyVector frequencyVector = builder.build();

    // should we support a merge function?
    // If so, it seems lke it would be nice for FrequencyVector to contain it's
    // sampling interval. Or we could just do error checks on length.
    // If we want this should we have:
    // 1. return a third merged thing?
    // 2. modify v1 or v2?
    FrequencyProtos.merge(v1, v2);
  }
}
