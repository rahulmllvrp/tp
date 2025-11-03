# Stress Testing Guide for AbsolutSin-ema

This guide explains how to stress test the AbsolutSin-ema application to evaluate its performance, stability, and scalability.

## Overview

The stress test suite includes three main categories:

1. **Large Dataset Stress Tests** - Test with thousands of contacts
2. **Performance Benchmark Tests** - Measure operation timing
3. **Memory Stress Tests** - Monitor memory consumption and detect leaks

## Quick Start

### Windows
```bash
stress-test.bat
```

### Mac/Linux
```bash
chmod +x stress-test.sh
./stress-test.sh
```

## Running Individual Test Suites

### 1. Large Dataset Stress Tests

Tests the application with increasing amounts of data:
- 1,000 persons
- 5,000 persons
- 10,000 persons
- Search performance in large datasets
- Persons with many tags
- Repeated add/remove operations

```bash
./gradlew test --tests "seedu.address.stress.LargeDatasetStressTest"
```

### 2. Performance Benchmark Tests

Measures the execution time of core operations:
- Add person
- Delete person
- Lookup person
- Update person
- Get filtered list

```bash
./gradlew test --tests "seedu.address.stress.PerformanceBenchmarkTest"
```

### 3. Memory Stress Tests

Monitors memory consumption:
- Adding 10,000 persons
- Repeated add/clear cycles (leak detection)
- Many tags per person
- Detailed memory statistics

```bash
./gradlew test --tests "seedu.address.stress.MemoryStressTest"
```

## Running All Stress Tests

```bash
./gradlew test --tests "seedu.address.stress.*"
```

## Viewing Results

After running the tests, view the detailed HTML report at:
```
build/reports/tests/test/index.html
```

## Manual JAR Stress Testing

### 1. Build the JAR
```bash
./gradlew shadowJar
```

### 2. Run with Memory Monitoring
```bash
java -Xmx512m -Xms256m -XX:+PrintGCDetails -jar build/libs/absolutsinema.jar
```

### 3. Monitor with JConsole
```bash
# In one terminal, run the app
java -jar build/libs/absolutsinema.jar

# In another terminal, start JConsole
jconsole
```

### 4. Monitor with VisualVM
```bash
# Start VisualVM
jvisualvm

# Then run your application
java -jar build/libs/absolutsinema.jar
```

## Performance Metrics

### Expected Performance Benchmarks

| Operation | Expected Time | Dataset Size |
|-----------|---------------|--------------|
| Add Person | < 1ms | 1,000 records |
| Delete Person | < 1ms | 1,000 records |
| Search Person | < 100μs | 1,000 records |
| Update Person | < 2ms | 1,000 records |
| Full Load (10K) | < 40s | 10,000 records |

### Expected Memory Usage

| Dataset Size | Expected Memory |
|--------------|-----------------|
| 100 persons | < 5 MB |
| 1,000 persons | < 20 MB |
| 5,000 persons | < 60 MB |
| 10,000 persons | < 100 MB |

## Creating Custom Stress Tests

To create your own stress test:

1. Create a new test class in `src/test/java/seedu/address/stress/`
2. Extend the stress test patterns shown in existing tests
3. Add your test to the stress test suite

Example:
```java
package seedu.address.stress;

import org.junit.jupiter.api.Test;
import seedu.address.model.AddressBook;

public class CustomStressTest {
    @Test
    public void myStressTest() {
        // Your stress test logic
    }
}
```

## Continuous Stress Testing

To integrate stress tests into your CI/CD pipeline:

```yaml
# Add to .github/workflows/gradle.yml
- name: Run Stress Tests
  run: ./gradlew test --tests "seedu.address.stress.*"
```

## Troubleshooting

### OutOfMemoryError
Increase heap size:
```bash
./gradlew test --tests "seedu.address.stress.*" -Dorg.gradle.jvmargs="-Xmx2g"
```

### Tests Taking Too Long
Run specific test categories instead of all tests:
```bash
./gradlew test --tests "seedu.address.stress.LargeDatasetStressTest.stressTest_add1000Persons_success"
```

### Memory Measurements Inaccurate
Ensure garbage collection runs between tests by adding delays or manual GC calls.

## Best Practices

1. **Run stress tests separately** from unit tests to avoid interference
2. **Close other applications** when running memory tests
3. **Run multiple times** to get consistent measurements
4. **Monitor system resources** during tests
5. **Document baselines** for comparison over time

## Advanced Testing

### Profile with JMH (Java Microbenchmark Harness)

For more accurate microbenchmarks, consider adding JMH:

```gradle
dependencies {
    implementation 'org.openjdk.jmh:jmh-core:1.36'
    annotationProcessor 'org.openjdk.jmh:jmh-generator-annprocess:1.36'
}
```

### Load Testing with TestFX

For UI stress testing:

```gradle
dependencies {
    testImplementation 'org.testfx:testfx-junit5:4.0.16-alpha'
}
```

## Further Resources

- [Java Performance Tuning Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/)
- [JConsole Documentation](https://docs.oracle.com/javase/8/docs/technotes/guides/management/jconsole.html)
- [VisualVM Documentation](https://visualvm.github.io/)
