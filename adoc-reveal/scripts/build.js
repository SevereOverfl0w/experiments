var asciidoctor = require('asciidoctor.js')();
var path = require('path');
var fs = require('fs');
require('asciidoctor-reveal.js');

// var baseDir = path.join(process.cwd(), process.argv[2])
// var outputDir = path.join(process.cwd(), process.argv[3])

function buildOne(file, outputDir) {
  var attributes = {'revealjsdir': 'reveal.js'};
  var options = {safe: 'safe',
                 backend: 'revealjs',
                 attributes: attributes,
                 to_dir: outputDir};

  asciidoctor.convertFile(file, options);
}

module.exports.buildOne = buildOne;
