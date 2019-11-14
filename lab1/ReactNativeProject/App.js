/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {
  View,
  Text,
  StatusBar,
  Button,
  TextInput,
  Image
} from 'react-native';

const App: () => React$Node = () => {
  const ButtonRow = () => (
      <View
        style={{
          marginTop: 16,
          flexDirection: 'row',
          justifyContent: 'space-evenly'
        }}>
          <Button title='Button'/>
          <Button title='Button'/>
      </View>
    );

  return (
    <>
      <StatusBar backgroundColor="#1a574b" barStyle="dark-content" />
      <View
        style={{
          padding: 16,
          backgroundColor:"#2d8577",
        }}>
          <Text
            style={{
              fontSize: 30,
              color: 'white'
            }}          
          >
            Example 1
          </Text>
        </View>
      <View
        style={{
          flex: 1,
          flexDirection: 'column',
          margin: 16
        }}>
          <View
            style={{
              flexDirection: "row",
              justifyContent: "space-around"
            }}
          >
            <Image style={{width:150, height:150}} source={require('./images/lab1_image_small.png')}/>
          </View>
          <ButtonRow/>
          <ButtonRow/>
          <View
            style={{
              flexDirection: 'row',
              marginTop: 16
            }}>
            <Text
            style={{
              flex: 1
            }}>
              Email
            </Text>
            <TextInput 
              style={{
                flex: 2,
                marginStart: 32,
                marginEnd: 32,
                borderBottomColor: "#ce2366",
                borderBottomWidth: 1,
                padding: 0
              }}/>
          </View>
        </View>
      </>
    );
};

export default App;
