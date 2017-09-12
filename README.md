# YahooWeather

[![](https://jitpack.io/v/coderJohnZhang/YahooWeather.svg)](https://jitpack.io/#coderJohnZhang/YahooWeather)

## Introduction

Use YahooWeather API to fetch the weather forecast

## Features

- Get weather information by city

- Locating city by the external network IP

- Use DataBinding and MVVM structure

- Update the weather every five minutes by using TimerTask

- Parse data with json and HttpURLConnection

- Call EventBus to send messages

## How to use

Step 1. Add the JitPack repository to your build file

### gradle
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
### maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
  
Step 2. Add the dependency

### gradle

	dependencies {
	        compile 'com.github.coderJohnZhang:YahooWeather:v1.0.1'
          compile 'org.greenrobot:eventbus:3.0.0'
          compile 'com.github.bumptech.glide:glide:3.5.2'
	}
	
### maven

	<dependency>
	    <groupId>com.github.coderJohnZhang</groupId>
	    <artifactId>YahooWeather</artifactId>
	    <version>v1.0.1</version>
	</dependency>

Step 3. Add databinding support

Add it in your module build.gradle

	android {  
	    ...  
	    ...  
	    ...  
	    dataBinding{  
		enabled true  
	    }  
	} 

## About me

Email: coder.john.cheung@gmail.com<br><br>
CSDN blog: http://blog.csdn.net/johnwcheung<br><br>
Github: https://github.com/coderJohnZhang

## License

		Copyright 2017 John Cheung

		Licensed under the Apache License, Version 2.0 (the "License");
		you may not use this file except in compliance with the License.
		You may obtain a copy of the License at

			http://www.apache.org/licenses/LICENSE-2.0

		Unless required by applicable law or agreed to in writing, software
		distributed under the License is distributed on an "AS IS" BASIS,
		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
		See the License for the specific language governing permissions and
		limitations under the License.

