// Karma configuration
module.exports = function (config) {
  config.set({
    basePath: '',
    frameworks: ['jasmine', '@angular-devkit/build-angular'],
    plugins: [
      require('karma-jasmine'),
      require('karma-chrome-launcher'),
      require('karma-jasmine-html-reporter'),
      require('@angular-devkit/build-angular/plugins/karma')
    ],
    client: { clearContext: false },
    reporters: ['progress', 'kjhtml'],
    browsers: ['ChromeHeadless'],
    singleRun: true
  });
};
