# Any Sketch

## Overview

This repo contains the implementation of the AnySketch data structure in Java.
AnySketch is a generic data structure that can be configured to act as

*   [HyperLogLog](https://datasketches.apache.org/docs/HLL/HLL.html)
*   [LiquidLegions](https://research.google/pubs/pub49177/)
*   [CascadingLegions](https://research.google/pubs/pub49177/)
*   Other sketches that can be defined with an index function and an aggregator.

## Details

AnySketch stores key-value pairs in a sparse format. AnySketch config is defined
by:

1.  A List of indexes, each with a Distribution,
1.  A List of value columns, each with a distribution,
1.  Aggregation functions for each value.

When a key is added to a sketch it is mapped to a vector of indexes and vector
of values. For each vector of indexes the vectors of values are aggregated with
the aggregation functions defined by the config.

## Usage

The libraries are
[available as Maven artifacts](https://github.com/orgs/world-federation-of-advertisers/packages?repo_name=any-sketch-java)
in the
[GitHub Packages Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).
Each artifact corresponds to a Java package under `org.wfanet`.

Note that native library dependencies are not included in the Maven artifacts.
These include the following:

*   `any-sketch`
    *   `libsketch_encrypter_adapter.so`
*   `estimation`
    *   `libestimators.so`
*   `frequency-count`
    *   `libsecret_share_generator_adapter.so`

These instead must be built separately and included in the JVM library path.
Pre-built libraries may be available as release assets.

### Examples

Let's say we want to create a counting Bloom Filter. Here is an example sketch
config:

```textproto
indexes {
  name: "Index"
  distribution {
    uniform {
      num_values: 10000000  # 10M
    }
  }
}
values {
  name: "Frequency"
  aggregator: SUM
  distribution {
    oracle {
      key: "frequency"
    }
  }
}
```

Create the sketch and insert elements to it:

```java
AnySketch anySketch = SketchProtos.toAnySketch(sketchConfig);
anySketch.insert(123456, ImmutableMap.of("frequency", 1L));
anySketch.insert(999999, ImmutableMap.of("frequency", 1L));
```

In this example, we did not store any values like demographics in the registers,
thus the empty HashMap. AnySketch hashed the keys (123456 and 999999),
distributed the hash uniformly to 10M values and put the value 1 in those two
registers. In the off chance they collided to the same register, because we are
using the UNIQUE aggregator, it marked that register with -1.

Here is an example where we are storing the demographics of each item:

```textproto
indexes {
  name: "Index"
  distribution {
    uniform {
      num_values: 10000000  # 10M
    }
  }
}
values {
  name: "SamplingIndicator"
  aggregator: UNIQUE
  distribution {
    uniform {
      num_values: 10000000  # 10M
    }
  }
}
values {
  name: "Demographics"
  aggregator: UNIQUE
  distribution {
    oracle {
      key: "demographics"
    }
  }
}
```

Create the sketch and insert elements to it:

```java
AnySketch anySketch = SketchProtos.toAnySketch(sketchConfig);
anySketch.insert(123456, ImmutableMap.of("demographics", 1L));
anySketch.insert(999999, ImmutableMap.of("demographics", 1L));
```
