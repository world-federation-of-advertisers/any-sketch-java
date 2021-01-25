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

import static com.google.common.collect.ImmutableMap.toImmutableMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import org.wfanet.anysketch.AnySketch;
import org.wfanet.anysketch.AnySketch.Register;

/**
 * Utilities to calculate the value histogram for the populated results of value function in a given
 * an AnySketch.
 */
public class ValueHistogram {

  public static ImmutableMap<Long, Double> calculateHistogram(
      AnySketch anySketch, String valueName, Predicate<Register> valueFilter) {
    final int valueIndex = anySketch.getValueIndex(valueName).getAsInt();
    ImmutableMap<Long, Long> unNormalized =
        StreamSupport.stream(anySketch.spliterator(), /*  parallel= */ false)
            .filter(valueFilter)
            .collect(toImmutableMap(r -> r.getValues().get(valueIndex), r -> 1L, Long::sum));
    Long total = unNormalized.values().stream().reduce(Long::sum).orElse(0L);

    return ImmutableMap.copyOf(Maps.transformValues(unNormalized, i -> (double) i / total));
  }
}
