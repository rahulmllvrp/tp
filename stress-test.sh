#!/bin/bash
# Stress Test Runner for AbsolutSin-ema
# This script runs various stress tests and monitors application performance

echo "========================================"
echo "AbsolutSin-ema Stress Test Suite"
echo "========================================"
echo ""

echo "[1/4] Running Large Dataset Stress Tests..."
echo "----------------------------------------"
./gradlew test --tests "seedu.address.stress.LargeDatasetStressTest"
echo ""

echo "[2/4] Running Performance Benchmark Tests..."
echo "----------------------------------------"
./gradlew test --tests "seedu.address.stress.PerformanceBenchmarkTest"
echo ""

echo "[3/4] Running Memory Stress Tests..."
echo "----------------------------------------"
./gradlew test --tests "seedu.address.stress.MemoryStressTest"
echo ""

echo "[4/4] Running all stress tests together..."
echo "----------------------------------------"
./gradlew test --tests "seedu.address.stress.*"
echo ""

echo "========================================"
echo "Stress Test Suite Complete!"
echo "========================================"
echo "Check build/reports/tests/test/index.html for detailed results"
echo ""
