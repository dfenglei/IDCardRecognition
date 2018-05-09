'use strict'

const { NativeModules } = require('react-native');
const { IDRecognitionModule } = NativeModules;

const REQUEST_CODE = 'requestCode';
const REQUEST_ID_FRONT = '15001';
const REQUEST_ID_BACK = '15002';

const DEFAULT_OPTIONS = {
  title: 'Select a Photo',
  cancelButtonTitle: 'Cancel',
  takePhotoButtonTitle: 'Take Photo…',
  chooseFromLibraryButtonTitle: 'Choose from Library…',
  quality: 1.0,
  allowsEditing: false,
  permissionDenied: {
    title: 'Permission denied',
    text: 'To be able to take pictures with your camera and choose images from your library.',
    reTryTitle: 're-try',
    okTitle: 'I\'m sure',
  }
};

module.exports = {
  ...IDRecognitionModule,
  showImagePicker: function showImagePicker(options, callback) {
    if (typeof options === 'function') {
      callback = options;
      options = {};
    }
    return IDRecognitionModule.showImagePicker({...DEFAULT_OPTIONS, ...options}, callback)
  },
    launchCamera: function showIdRecognitionScreen(options, callback) {
    if (typeof options === 'function') {
      callback = options;
      options = {};
    }
    return IDRecognitionModule.launchCamera({...DEFAULT_OPTIONS, ...options}, callback)
  },
    showIdRecognitionFrontScreen: function showIdRecognitionScreen(options, callback) {
        if (typeof options === 'function') {
            callback = options;
            options = {};
        }
        console.log(IDRecognitionModule);
        return IDRecognitionModule.showIdRecognitionScreen({...DEFAULT_OPTIONS, ...options,[REQUEST_CODE]:REQUEST_ID_FRONT}, callback)
    },
    showIdRecognitionBackScreen: function showIdRecognitionScreen(options, callback) {
        if (typeof options === 'function') {
            callback = options;
            options = {};
        }
        console.log(IDRecognitionModule);
        return IDRecognitionModule.showIdRecognitionScreen({...DEFAULT_OPTIONS, ...options,[REQUEST_CODE]:REQUEST_ID_BACK}, callback)
    }
}
