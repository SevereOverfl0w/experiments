var build = require('./build');
var path = require('path');
var chokidar = require('chokidar');

function watch(inputDir, outputDir){
  function watchHandler(path) {
    build.buildOne(path, outputDir);
  }
  chokidar.watch(inputDir)
    .on('add', watchHandler)
    .on('change', watchHandler)
}

exports.watch = watch;
