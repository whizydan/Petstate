# SHAREthem
![App_Icon](app/src/main/ic_launcher-playstore.png?raw=true "app-icon")

Petstate facilitates communication between vets and their clients remotely. 
 
* also supports App backup to firebase incase the app is reinstalled so remember to use your own google-json file and enable firebase realtime database.

* also includes payment with daraja api,which you have to change the callback url in code to point to your own location for the api.

* the api folder contains files used in the backened for payment verification.

 

### Features

* In app messaging.
* Backup restore of user data.
* Daraja api for payment.
* Authentication by email using firebase
* persistent data using sqlite

### Usage


## IMPORTANT NOTE
* Careless api overrides were done since this was a beginners project.Code might break where version of BUILD has not been handled.
* some UI elements are not in their right position.


## Screenshots
![Initial Activity](demos/initial.png?raw=true "Permission request")
![login](demos/login.png?raw=true "android login")
![Restore Activity](demos/restorebackup.png?raw=true "Restore ackup data")
![Downloading](demos/progress.png?raw=true "Dowload db")
![Complete](demos/restored.png?raw=true "Finjished restore")
![Register](demos/register.png?raw=true "new users")
![Vets homepage](demos/main.png?raw=true "vet homepage")
![pet owners homepage](demos/main2.png?raw=true "client homepage")
![add data page](demos/adddata.png?raw=true "New conditions")
![Vets chat](demos/chat.png?raw=true "vet chat")
![client chat](demos/chat2.png?raw=true "client chat")
![client billing](demos/paymentapi.png?raw=true "paying before chat")
![Reset password](demos/resetpass.png?raw=true "forgot password")
## License

Copyright 2022 

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
 Happy coding😎
