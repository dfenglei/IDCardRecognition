import React, { Component } from "react";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";
import RNIDRecognition from "react-native-IDRecognition";

export default class App extends Component<> {
    render() {
        return (
            <View style={styles.container}>
                <View style={{ flex : 0, flexDirection : 'row', justifyContent : 'center', }}>
                    <TouchableOpacity
                        onPress={this.showIdRecognitionFrontScreen.bind(this)}
                        style={styles.capture}
                    >
                        <Text style={{ fontSize : 14 }}> showIdRecognitionFrontScreen </Text>
                    </TouchableOpacity>
                </View>
                <View style={{ flex : 0, flexDirection : 'row', justifyContent : 'center', }}>
                    <TouchableOpacity
                        onPress={this.showIdRecognitionBackScreen.bind(this)}
                        style={styles.capture}
                    >
                        <Text style={{ fontSize : 14 }}> showIdRecognitionBackScreen </Text>
                    </TouchableOpacity>
                </View>
            </View>
        );
    }

    showIdRecognitionFrontScreen = async function () {
        // alert('111');
        RNIDRecognition.showIdRecognitionFrontScreen({}, (data) => {
            console.log(data);
            alert(data);
        });
    };
    showIdRecognitionBackScreen = async function () {
        // alert('111');
        RNIDRecognition.showIdRecognitionBackScreen({}, (data) => {
            console.log(data);
            alert(data);
        });
    };

}

const styles = StyleSheet.create({
    container : {
        flex : 1,
        flexDirection : 'column',
        backgroundColor : 'black'
    },
    preview : {
        flex : 1,
        justifyContent : 'flex-end',
        alignItems : 'center'
    },
    capture : {
        flex : 0,
        backgroundColor : '#fff',
        borderRadius : 5,
        padding : 15,
        paddingHorizontal : 20,
        alignSelf : 'center',
        margin : 20
    }
});
