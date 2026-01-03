 // Import the functions you need from the SDKs you need
 import { initializeApp } from "https://www.gstatic.com/firebasejs/9.1.3/firebase-storage.js";
 import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.1.3/firebase-analytics.js";
 // TODO: Add SDKs for Firebase products that you want to use
 // https://firebase.google.com/docs/web/setup#available-libraries

 // Your web app's Firebase configuration
 // For Firebase JS SDK v7.20.0 and later, measurementId is optional
 const firebaseConfig = {
   apiKey: "YOUR_API_KEY",
   authDomain: "YOUR_AUTH_DOMAIN",
   projectId: "YOUR_PROJECT_ID",
   storageBucket: "YOUR_STORAGE_BUCKET",
   messagingSenderId: "YOUR_SENDER_ID",
   appId: "YOUR_APP_ID",
   measurementId: "YOUR_MEASUREMENT_ID"
 };

 // Initialize Firebase
 const app = initializeApp(firebaseConfig);
 const analytics = getAnalytics(app);
 console.log(firebase)
 function uploadImage() {
   const ref = firebase.storage().ref();
   const file = document.querySelector("#photo").files[0];
   const name = new Date() + "-" + file.name;
   const metadata = {
     contentType: file.type
   };
   const task = ref.child(name).put(file, metadata);
   task
     .then(snapshot => snapshot.ref.getDownloadURL())
     .then(url => {
       console.log(url);
       alert("Image Upload Successful")
       
       document.querySelector("#image").src = url;
     })
     .catch(console.error);
 }