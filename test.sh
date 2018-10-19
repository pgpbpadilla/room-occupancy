#!/usr/bin/env bash
curl -v -H "Content-Type: application/json" \
    --data @test.json \
    http://localhost:8080/summary
