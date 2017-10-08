#!/usr/bin/env node
var liveServer = require("live-server");
var path = require('path');
var watch = require('./watch');

inputDir = path.join(process.cwd(), process.argv[2])
outputDir = path.join(process.cwd(), process.argv[3])

var params = {
	root: outputDir,
	open: true,
	wait: 1000,
	mount: [['/reveal.js', path.join(require.resolve('reveal.js'), '../../')]],
};

watch.watch(inputDir, outputDir)
liveServer.start(params);
