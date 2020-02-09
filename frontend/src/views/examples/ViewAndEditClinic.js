/*!
 
=========================================================
* Paper Kit React - v1.0.0
=========================================================
 
* Product Page: https://www.creative-tim.com/product/paper-kit-react
 
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/paper-kit-react/blob/master/LICENSE.md)
 
* Coded by Creative Tim
 
=========================================================
 
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 
*/
import React , {Component} from "react";
import axios from 'axios';

import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
 
 
// reactstrap components
import {
  Button,
  FormGroup,
  Input,
  Container,
  Row,
  Col,
  Form,
  Modal,
} from "reactstrap";
 
// core components
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';
import  MapGoogle  from "./MapGoogle";
import {Line} from 'react-chartjs-2';
 
 
/*let tempGraph = {
  labels: ['Januar', 'Februar', 'Mart', 'April', 'Maj', 'Jun', 'Jul', 'Avgust', 'Septembar', 'Oktobar', 'Novembar', 'Decembar'],
  datasets: [
    {
      label: 'Broj pregleda na nivou mjeseci',
      fill: false,
      lineTension: 0.1,
      backgroundColor: 'rgba(75,192,192,0.4)',
      borderColor: 'rgba(75,192,192,1)',
      borderCapStyle: 'butt',
      borderDash: [],
      borderDashOffset: 0.0,
      borderJoinStyle: 'miter',
      pointBorderColor: 'rgba(75,192,192,1)',
      pointBackgroundColor: '#fff',
      pointBorderWidth: 1,
      pointHoverRadius: 5,
      pointHoverBackgroundColor: 'rgba(75,192,192,1)',
      pointHoverBorderColor: 'rgba(220,220,220,1)',
      pointHoverBorderWidth: 2,
      pointRadius: 1,
      pointHitRadius: 10,
      data: [65, 59, 80, 81, 56, 55, 40, 34, 90, 40, 18, 3]
    }
  ]
};
 
 
let tempGraph2 = {
  labels: ['Dan1', 'Dan2', 'Dan3', 'Dan4', 'Dan5', 'Dan6', 'Dan7'],
  datasets: [
    {
      label: 'Broj pregleda na nivou sedmice',
      fill: false,
      lineTension: 0.1,
      backgroundColor: 'rgba(75,192,192,0.4)',
      borderColor: 'rgba(75,192,192,1)',
      borderCapStyle: 'butt',
      borderDash: [],
      borderDashOffset: 0.0,
      borderJoinStyle: 'miter',
      pointBorderColor: 'rgba(75,192,192,1)',
      pointBackgroundColor: '#fff',
      pointBorderWidth: 1,
      pointHoverRadius: 5,
      pointHoverBackgroundColor: 'rgba(75,192,192,1)',
      pointHoverBorderColor: 'rgba(220,220,220,1)',
      pointHoverBorderWidth: 2,
      pointRadius: 1,
      pointHitRadius: 10,
      data: [5, 10, 3, 10, 4, 3, 9]
    }
  ]
};
 */
 
class ViewAndEditClinic extends Component {
  constructor(props)
  {
    super(props);
 
    this.state = {
      dataShow: false,
      name: "Bolnica" ,
      description: "" ,
      prize: "" ,
      address: "Savski venac" ,  
      city: "Beograd",
      drzava: "Bosna i Hercegovina",
      temp:true,
      raiting:"",
      city1:"",
      temp1:false,
      name1:"",
      description1:"",
      prize1:"",
      address1:"",
      nameValidation:"",
      descriptionValidation:"",
      cityValidation:"",
      prizeValidation:"",
      address1Validation:"",
      message1:"",
      rooms: [],
      roomName: "",
      roomNumber: undefined,
      roomNumVal: "",
      roomNameVal: "",
      roomVal: "",
      newRoomShow: false,
      showAddType: false,
      tipovi:[],
      tipName:"",
      pomType:[],
      message1Type:"",
      vrstaSale:"",
      cijenaTipa:"",
      tempGraph: [],
      tempGraph2: [],
      values: [],
      pom1:[],
      pom2:[],
      showPrihodi: false,
      prihodStart:"",
      prihodEnd:"",
      prihodiValidacija1:"",
      prihodiValidacija2:"",
      prihodiMessage:"",
      prihodiMessage1:"",
      danasnjiPrihod:"",
    };
 
    this.updateOneClinic = this.updateOneClinic.bind(this);
    this.addRoom = this.addRoom.bind(this);
    this.addType = this.addType.bind(this);
    this.getPrihodi = this.getPrihodi.bind(this);
  }
     
  doc = document.documentElement.classList.remove("nav-open");
 
  //React.useEffect(() => {
    //document.body.classList.add("landing-page");
    //return function cleanup() {
      //document.body.classList.remove("landing-page");
   // };
 // });
 
 
 addRoom = event => {
  event.preventDefault();
 
  let number = this.state.roomNumber;
  let name = this.state.roomName;
  let ok = true;
 
  if (number === undefined || number === ''){
      this.setState({roomNumVal:"Broj sale je obavezno polje"})
      ok = false;
  }
  else if (number <= 0){
      this.setState({roomNumVal:"Broj sale mora biti pozitivan."})
      ok = false;
  }
  else
      this.setState({roomNumVal:""})
 
  if (name === undefined || name === ''){
      this.setState({roomNameVal:"Naziv sale je obavezno polje"})
      ok = false;
  }
  else
      this.setState({roomNameVal:""})
 
  if(ok){
     let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      let data = {
      "name": this.state.roomName ,  
      "number": this.state.roomNumber ,
      "roomType": this.state.vrstaSale,
      };        
      axios({
        method: 'post',
        url: 'http://localhost:8099/clinic/addRoom',
        data: data,
        headers: { "Authorization": AuthStr }  
      }).then((response) => {
        NotificationManager.success('Uspjesno dodata soba!', 'Uspjesno!', 3000);
        console.log(response);
        this.state.rooms.push(data)
        this.setState({newRoomShow: false});
        this.setState({roomVal:""})
       
      }, (error) => {
        console.log(error);
        if(error.status === "ALREADY_REPORTED")
       // alert("Sala sa ovim imenom ili brojem vec postoji")
       NotificationManager.error('Greska prilikom dodavanja sobe!', 'Greska!', 3000);
      });
 
  }
};
 
 
 
getPrihodi = event => {
  event.preventDefault();
 
  let ddatum1 = this.state.prihodStart;
  let ddatum2 = this.state.prihodEnd;
  let ok = true;
  let godina;
  let mjesec;
  let dan;
  godina = ddatum1[0] + ddatum1[1]+ddatum1[2]+ddatum1[3]
 
  mjesec = ddatum1[5] + ddatum1[6]
 
  dan = ddatum1[8] + ddatum1[9]
 
  let newDate = new Date();
  let date = parseInt(newDate.getDate())
  let month = parseInt(newDate.getMonth() + 1);
  let year = parseInt(newDate.getFullYear());
  godina = parseInt(godina)
  mjesec = parseInt(mjesec)
  dan = parseInt(dan)
 
 
if(ddatum1=== undefined || ddatum1 === ''){
  this.setState({prihodiMessage:"Datum je obavezno polje"})
  ok = false;
}
  else if(godina > year){
    this.setState({prihodiMessage:"Godina je veca"})
    ok = false;
  }
  else if(mjesec> month && godina>=year){
    this.setState({prihodiMessage:"Mjesec je veci"})
    ok = false;
  }
  else if(dan>date && mjesec>=month && godina>=year ){
    this.setState({prihodiMessage:"Dan je veci"})
    ok = false;
  }
  else {
    this.setState({prihodiMessage:""})
}
 
 
godina = ddatum2[0] + ddatum2[1]+ddatum2[2]+ddatum2[3]
 
mjesec = ddatum2[5] + ddatum2[6]
 
dan = ddatum2[8] + ddatum2[9]
 
 newDate = new Date();
 date = parseInt(newDate.getDate());
 month = parseInt(newDate.getMonth() + 1);
 year = parseInt(newDate.getFullYear());
godina = parseInt(godina)
mjesec = parseInt(mjesec)
dan = parseInt(dan)
 
 
if(ddatum2=== undefined || ddatum2 === ''){
  this.setState({prihodiMessage1:"Datum je obavezno polje"})
  ok = false;
}
else if(godina > year){
  this.setState({prihodiMessage1:"Godina je veca"})
  ok = false;
}
else if(mjesec> month && godina>=year){
  this.setState({prihodiMessage1:"Mjesec je veci"})
  ok = false;
}
else if(dan>date && mjesec>=month && godina>=year ){
  this.setState({prihodiMessage1:"Dan je prosao"})
  ok = false;
}
else {
  this.setState({prihodiMessage1:""})
}
 
if(ok){
    let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
  let data = [];
  data.push(ddatum1);
  data.push(ddatum2);
  axios({
    method: 'post',
    url: 'http://localhost:8099/clinic/getRevenue',
    data: data,
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
    console.log(response);
   this.setState({danasnjiPrihod:response.data});
  }, (error) => {
    console.log(error);
    if(error.status === 208)
    this.setState({prihodiMessage: "Prikazan je danasnji prihod, za vise unesite dobre datume"})
  });
}
 
};
 
 
 
addType = event => {
  event.preventDefault();
 
  let name = this.state.tipName;
  let number = this.state.cijenaTipa;
  let ok = true;
 
  for(let i= 0; i <this.state.tipovi.length; i++){
    if(name === this.state.tipovi[i].name){
      this.setState({message1Type:"Naziv vec postoji!"})
      ok = false;
    }
  }
 
  if (name === undefined || name === ''){
      this.setState({messageType:"Naziv tipa je obavezno polje"})
      ok = false;
  }
  else
      this.setState({messageType:""})
 
      if (number === undefined || number === ''){
        this.setState({messageType:"Cijena pregleda je obavezno polje"})
        ok = false;
      }
    else if (number <= 0 || number > 1000){
        this.setState({messageType:"Opseg cijene se krece od 1 do 1000"})
        ok = false;
    }
    else{
        this.setState({messageType:""})
    }
 
 
  if(ok){
      let data = {
      "name": this.state.tipName ,
      "typePrice": this.state.cijenaTipa,
      };        
      // ovo treba u response ok
      this.setState({temp1: false});
      this.state.tipovi.push(data)
      this.setState({showAddType: false});
      this.setState({messageType:""})
      this.setState({tipName:""})
      this.state.pomType.push(name);
      this.setState({showAddType: false});
 
      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
 
      axios({
        method: 'post',
        url: 'http://localhost:8099/checkUpType/addType',
        data: data,
        headers: { "Authorization": AuthStr }  
      }).then((response) => {
        console.log(response);
        NotificationManager.success('Uspjesno dodavanje tipa!', 'Uspjesno!', 3000);
      }, (error) => {
        console.log(error);
        //if(error.status === "ALREADY_REPORTED")
        this.setState({message: "Tip pregleda sa ovim imenom u klinici vec postoji"})
        NotificationManager.info('Tip sa ovim imenom vec postoji u klinici!', 'Info!', 3000);
      });
       
  }
};
 
 
roomNameValidation(e){
  this.setState({roomName:e.target.value})
  let name = e.target.value;
  if (name === undefined || name === '')
      this.setState({roomNameVal:"Naziv sale je obavezno polje."})
  else
      this.setState({roomNameVal:""})
};
 
 
typeValidation(e){
  this.setState({tipName:e.target.value})
  let name = e.target.value;
 
  for(let i = 0; i<this.state.tipovi; i++){
     if(this.state.tipovi.getItem(i).match(name)){
        this.setState({messageType:"Tip sa ovim nazivom vec postoji!"})
      }
  }
  if (name === undefined || name === '')
      this.setState({messageType:"Naziv tipa je obavezno polje."})
  else
      this.setState({messageType:""})
};
 
 
deleteType(name, e){
  e.preventDefault();
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  axios({
    method: 'post',
    url: 'http://localhost:8099/checkUpType/deleteType/' + name,
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
     NotificationManager.success('Tip uspjesno izbrisan!', 'Uspjesno!', 3000);
    console.log(response);
  }, (error) => {
    console.log(error);
    if(error.status === "ALREADY_REPORTED")
    this.setState({message: "Greska u brisanju tipa"})
    NotificationManager.error('Greska brisanja tipa!', 'Greska!', 3000);
  });
 // ovo u ok responsu
  const items = this.state.tipovi.filter(tip => tip.name !== name);
  this.setState({ tipovi: items });
};
 
 deleteRoom(name, e){
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  axios({
    method: 'post',
    url: 'http://localhost:8099/clinic/deleteRoom/' + name,
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
    console.log(response);
     NotificationManager.success('Uspjesno izbrisana soba!', 'Uspjesno!', 3000);
  }, (error) => {
    console.log(error);
    if(error.status === 208){
       this.setState({message: "Greska u brisanju sale"})
        NotificationManager.error('Greska brisanja sale!', 'Greska!', 3000);
    }
   
  const items = this.state.rooms.filter(room => room.name !== name);
  this.setState({ rooms: items });
});
 };
 
 
 
 
 
roomNumberValidation(e){
  this.setState({roomNumber:e.target.value})
  let number = e.target.value;
  if (number === undefined || number === '')
      this.setState({roomNumVal:"Broj sale je obavezno polje"})
  else if (number <= 0)
      this.setState({roomNumVal:"Broj sale mora biti pozitivan."})
  else
      this.setState({roomNumVal:""})
};
 
 componentDidMount(){
  this.setState({vrstaSale:"PREGLED"})
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
 
  axios({
    method: 'get',
    url: 'http://localhost:8099/clinic/getClinic',
    headers: { "Authorization": AuthStr }  
  }).then((response)=>{
    console.log(response);
    this.setState({name:response.data.name, address:response.data.address,description:response.data.description,raiting:response.data.rating,city:response.data.city})
  },(error)=>{
    console.log(error);
  });
 
  axios({
    method: 'get',
    url: 'http://localhost:8099/clinic/getRooms',
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
    console.log(response);
      this.setState({rooms:response.data});
  }, (error) => {
    console.log(error);
  });
 
  axios({
    method: 'get',
    url: 'http://localhost:8099/clinic/getReportForMonth',
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
    console.log(response);
     this.response = response.data; 
     let pom1 = {
      labels: ['Januar', 'Februar', 'Mart', 'April', 'Maj', 'Jun', 'Jul', 'Avgust', 'Septembar', 'Oktobar', 'Novembar', 'Decembar'],
      datasets: [
        {
          label: 'Broj pregleda na nivou mjeseci',
          fill: false,
          lineTension: 0.1,
          backgroundColor: 'rgba(75,192,192,0.4)',
          borderColor: 'rgba(75,192,192,1)',
          borderCapStyle: 'butt',
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: 'miter',
          pointBorderColor: 'rgba(75,192,192,1)',
          pointBackgroundColor: '#fff',
          pointBorderWidth: 1,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: 'rgba(75,192,192,1)',
          pointHoverBorderColor: 'rgba(220,220,220,1)',
          pointHoverBorderWidth: 2,
          pointRadius: 1,
          pointHitRadius: 10,
          data: response.data,
        }
      ]
    };
    this.setState({pom1:pom1})
 
    // console.log(tempGraph.datasets[0].data)
  }, (error) => {
    console.log(error);
  });
 
 
  axios({
    method: 'get',
    url: 'http://localhost:8099/clinic/getReportForWeek',
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
    console.log(response);
     this.response = response.data;
 
     let pom2 = {
      labels: ['Dan1', 'Dan2', 'Dan3', 'Dan4', 'Dan5', 'Dan6', 'Dan7'],
      datasets: [
        {
          label: 'Broj pregleda na nivou sedmice',
          fill: false,
          lineTension: 0.1,
          backgroundColor: 'rgba(75,192,192,0.4)',
          borderColor: 'rgba(75,192,192,1)',
          borderCapStyle: 'butt',
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: 'miter',
          pointBorderColor: 'rgba(75,192,192,1)',
          pointBackgroundColor: '#fff',
          pointBorderWidth: 1,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: 'rgba(75,192,192,1)',
          pointHoverBorderColor: 'rgba(220,220,220,1)',
          pointHoverBorderWidth: 2,
          pointRadius: 1,
          pointHitRadius: 10,
          data: response.data,
        }
      ]
    };
    this.setState({pom2:pom2})
 
    // console.log(tempGraph.datasets[0].data)
  }, (error) => {
    console.log(error);
  });
 
 
 
  axios({
    method: 'get',
    url: 'http://localhost:8099/clinic/getAllTypes',
    headers: { "Authorization": AuthStr }  
  }).then((response) => {
    console.log(response);
      this.setState({tipovi:response.data})
  }, (error) => {
    console.log(error);
  });
 }
   
 
 updateOneClinic = event => {
    event.preventDefault();
    let isOk = true;
 
      let nameMy = this.state.name1
      let descriptionMy = this.state.description1;
      let cityMy = this.state.city1;
      let addressMy = this.state.address1;
 
      if (nameMy === undefined || nameMy === ''){
        this.setState({nameValidation:"Naziv je obavezno polje."})
        isOk = false;
      }
      else if(!nameMy[0].match('[A-Z]')){
        this.setState({nameValidation:"Naziv klinike mora pocinjati velikim slovom."})
        isOk = false;
     }
      else{
        this.setState({nameValidation:""})
      }
 
      if (descriptionMy === undefined || descriptionMy === ''){
        this.setState({descriptionValidation:"Opis je obavezno polje."})
        isOk = false;
      }
      else if(!descriptionMy[0].match('[A-Z]')){
        this.setState({descriptionValidation:"Opis mora pocinjati velikim slovom."})
        isOk = false;
     }
      else{
        this.setState({descriptionValidation:""})
      }
      if (cityMy === undefined || cityMy === ''){
        this.setState({city1Validation:"Grad je obavezno polje."})
        isOk = false;
      }
      else if(!cityMy[0].match('[A-Z]')){
        this.setState({city1Validation:"Grad klinike mora pocinjati velikim slovom."})
        isOk = false;
     }
      else{
        this.setState({cityValidation:""})
      }
 
 
      if (addressMy === undefined || addressMy === ''){
        this.setState({address1Validation:"Adresa je obavezno polje."})
        isOk = false;
      }
      else if(!addressMy[0].match('[A-Z]')){
        this.setState({address1Validation:"Adresa klinike mora pocinjati velikim slovom."})
        isOk = false;
     }
      else{
        this.setState({address1Validation:""})
      }
     
     
 
  if(isOk){
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
 
    this.setState({name:this.state.name1, description:this.state.description1, city:this.state.city1, address:this.state.address1})
        let data = {
          "name": this.state.name1 ,
          "description": this.state.description1 ,
          "city": this.state.city1 ,
          "address": this.state.address1,
          "raiting": this.state.raiting ,
        //  "rooms":this.state.rooms,
          "types":this.state.tipovi,
      };
 
    axios({
      method: 'post',
      url: 'http://localhost:8099/clinic/updateClinic',
      data: data,
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
      this.setState({temp1: false});
       NotificationManager.success('Uspjesna izmjena klinike!', 'Uspjesno!', 3000);
    }, (error) => {
      console.log(error);
      if(error.status === "ALREADY_REPORTED")
       NotificationManager.error('Klinika sa ovim nazivom vec postoji!', 'Greska!', 3000);
      this.setState({message: "Klinika sa ovim nazivom vec postoji."})
    });
    this.setState({temp1: false});
 
    //this.setState({temp1: false});
  }
 
};
 
validacija(e) {
  this.setState({name1 : e.target.value});
  let sun = e.target.value;
  if(sun===''){
    this.setState({nameValidation : "Ovo polje ne moze biti prazno", flag1:false});
  }
  else if(!sun[0].match('[A-Z]')){
    this.setState({nameValidation : "Ime mora da pocinje velikim slovom", flag1:false});
  }
  else{
    this.setState({nameValidation : "", flag1:true});
  }
 
  e.preventDefault();
}
 
promjenaTipaSale(e){
  this.setState({vrstaSale:e.target.value});
}
 
validacija1(e) {
  this.setState({description1 : e.target.value});
  let sun = e.target.value;
  if(sun===''){
    this.setState({descriptionValidation : "Ovo polje ne moze biti prazno", flag2:false});
  }
  else if(!sun[0].match('[A-Z]')){
    this.setState({descriptionValidation : "Opis mora poceti velikim slovom", flag2:false});
  }
  else{
    this.setState({descriptionValidation : "", flag2:true});
  }
 
  e.preventDefault();
}
 
validacija2(e) {
    this.setState({city1 : e.target.value});
    let sun = e.target.value;
    if(sun===''){
      this.setState({city1Validation : "Ovo polje ne moze biti prazno", flag2:false});
    }
    else if(!sun[0].match('[A-Z]')){
      this.setState({city1Validation : "Grad mora poceti velikim slovom", flag2:false});
    }
    else{
      this.setState({city1Validation : "", flag2:true});
    }
 
    e.preventDefault();
  }
 
 
 
cijenaTipaValidacija(e) {
  this.setState({cijenaTipa : e.target.value});
  let number = e.target.value;
  if (number === undefined || number === '')
      this.setState({messageType:"Cijena pregleda je obavezno polje"})
  else if (number <= 0 || number > 1000)
      this.setState({messageType:"Opseg cijene se krece od 1 do 1000"})
  else
      this.setState({messageType:""})
}
 
 
validacija3(e) {
    this.setState({address1 : e.target.value});
    let sun = e.target.value;
    if(sun===''){
      this.setState({address1Validation : "Ovo polje ne moze biti prazno", flag2:false});
    }
    else if(!sun[0].match('[A-Z]')){
      this.setState({address1Validation : "Adresa mora poceti velikim slovom", flag2:false});
    }
    else{
      this.setState({address1Validation : "", flag2:true});
    }
 
    e.preventDefault();
}
prihodiValidacija(e){
   
  let ddatum = e.target.value;
  let godina;
  let mjesec;
  let dan;
  godina = ddatum[0] + ddatum[1]+ddatum[2]+ddatum[3]
 
  mjesec = ddatum[5] + ddatum[6]
 
  dan = ddatum[8] + ddatum[9]
 
  let newDate = new Date();
  let date = parseInt(newDate.getDate())
  let month = parseInt(newDate.getMonth() + 1);
  let year = parseInt(newDate.getFullYear());
  godina = parseInt(godina)
  mjesec = parseInt(mjesec)
  dan = parseInt(dan)
 
 
if(ddatum=== undefined || ddatum === ''){
  this.setState({prihodiMessage:"Datum je obavezno polje"})
}
  else if(godina > year){
    this.setState({prihodiMessage:"Godina je veca"})
  }
  else if(mjesec> month && godina>=year){
    this.setState({prihodiMessage:"Mjesec je veci"})
  }
  else if(dan>date && mjesec>=month && godina>=year ){
    this.setState({prihodiMessage:"Dan je veci"})
  }
  else {
    this.setState({prihodiMessage:""})
}
  this.setState({prihodStart:e.target.value})
 
};
 
 
prihodiValidacija1(e){
   
  let ddatum = e.target.value;
  let godina;
  let mjesec;
  let dan;
  godina = ddatum[0] + ddatum[1]+ddatum[2]+ddatum[3]
 
  mjesec = ddatum[5] + ddatum[6]
 
  dan = ddatum[8] + ddatum[9]
 
  let newDate = new Date();
  let date = parseInt(newDate.getDate())
  let month = parseInt(newDate.getMonth() + 1);
  let year = parseInt(newDate.getFullYear());
  godina = parseInt(godina)
  mjesec = parseInt(mjesec)
  dan = parseInt(dan)
 
 
if(ddatum=== undefined || ddatum === ''){
  this.setState({prihodiMessage1:"Datum je obavezno polje"})
}
  else if(godina > year){
    this.setState({prihodiMessage1:"Godina je veca"})
  }
  else if(mjesec> month && godina>=year){
    this.setState({prihodiMessage1:"Mjesec je veci"})
  }
  else if(dan>date && mjesec>=month && godina>=year ){
    this.setState({prihodiMessage1:"Dan je prosao"})
  }
  else {
    this.setState({prihodiMessage1:""})
}
  this.setState({prihodEnd:e.target.value})
 
};
 
 
logoutUser = () => {  
  localStorage.removeItem('ulogovan')
  localStorage.removeItem('role')
  this.props.history.push('/register-page');
}
 
redirect = () => {
  this.props.history.push('/administrator-page');
}
 
render() {
 
 
 
  return (
    <>
     
      <ExamplesNavbar logoutEvent={this.logoutUser}
                      showProfileEvent={this.redirect}
                      hideKalendar={true}
                      hideNewWorker = {true}
                      hideNewQuick = {true}
                      hideReceipts = {true}
                      hideTypeAdmin = {true}                     
                      hideRequestsAdmin = {true}
                      hidePregledi = {true}
                      hidePatientKlinike = {true}
                      hideCheckupDoctor = {true}
                      hideRoomsAdmin = {true}
                      hideDocsAdmin = {true}
                      hideClinics = {true}                     
                      hideAddNewClinic = {true}
                      hidePatientsDoc = {true}
                      hideVacation = {true}                     
                      sakrij = {true}
                      hideAllQuicksEvent = {true}                      
                      hideKarton = {true}
                       hideRegisterEvent = {true}
                       hideLoginEvent = {true}     />
 
      <ProfilePageHeader />
 
      <div className="section profile-content">
        <Container>
          <div className="owner">
            <div className="avatar">
              <img
                alt="..."
                className="img-circle img-no-padding img-responsive"
                src={require("assets/img/hc-complete-resize.jpg")}
              />
            </div>
            <div className="name">
              <h4  value = {this.state.name} className="title">{this.state.name}</h4>
             <br />
            </div>
          </div>
          <Row>
            <Col className="ml-auto mr-auto text-center" md="6">
              <p font size="3" color="red">
                {this.state.description}
              </p>
              <br />
             
            </Col>
          </Row>
          <br />
 
 
 
          <Modal modalClassName="modal-register" isOpen={this.state.newRoomShow}>
     
      <div className="modal-header no-border-header text-center">
          <button
              aria-label="Close"
              className="close"
              data-dismiss="modal"
              type="button"
              onClick={() => this.setState({newRoomShow: false, roomNumVal:"", roomNameVal:""})}
          >
          <span aria-hidden={true}>×</span>
          </button>
          <h3 className="title mx-auto">Nova sala</h3>
      </div>
      <div className="modal-body">                      
              <Form onSubmit={this.addRoom}>
                  <FormGroup>
                  <label>Naziv</label>
                  <Input name="roomName" type="text" onChange={(event) => this.roomNameValidation(event)} />
                  <p style={{color:'red'}}>{this.state.roomNameVal}</p>
                  </FormGroup>
                  <FormGroup>
                  <label>Broj</label>
                  <Input name="roomNumber" type="number" onChange={(event) => this.roomNumberValidation(event)} />
                  <p style={{color:'red'}}>{this.state.roomNumVal}</p>
                  </FormGroup>      
                  <FormGroup>
                    <label> Tip sale &nbsp;&nbsp;&nbsp; </label>
                     <select value={this.state.vrstaSale}
                      onChange={(e) =>  this.promjenaTipaSale(e)}>
                      <option value={"PREGLED"}>Pregled</option>
                      <option value={"OPERACIJA"}>Operacija</option>
                    </select>
                  </FormGroup>  
                  <Button block className="btn-round" color="info">
                       Dodaj salu
                  </Button>
               
                  </Form>
      </div>
      </Modal>
 
 
 
      <Modal  modalClassName="modal-register" isOpen={this.state.showPrihodi}>
<div className="modal-header no-border-header text-center">
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({showPrihodi:false, prihodiValidacija:"", prihodiValidacija1:"", prihodiMessage:""} )} // na izlaz
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body">
          <Form onSubmit={this.getPrihodi} >
        <FormGroup>
          <div className="row">
          <div className="col-xs-3">
        <label>&nbsp;&nbsp;&nbsp; </label>
       
        </div>
          <div className="col-xs-3">
        <label>Od: &nbsp;&nbsp;&nbsp; </label>
        <Input  name="datumPocetak"  value = {this.state.prihodStart}  onChange={(event) => this.prihodiValidacija(event)} type="date"  />
        <p style={{color:'red'}}>{this.state.prihodiMessage}</p>
        </div>
        <div className="col-xs-3">
        <label>Do:  &nbsp;&nbsp;&nbsp;</label>
        <Input  name="datumKraj"  value = {this.state.prihodEnd}  onChange={(event) => this.prihodiValidacija1(event)} type="date"  />
        <p style={{color:'red'}}>{this.state.prihodiMessage1}</p>
 
        </div>
       
  <p style={{color:'blue'}}>Ukupan prihod u trazenom periodu je : {this.state.danasnjiPrihod}</p>
        </div>
        <br></br>
       
        </FormGroup>
        <Button block className="btn-round" color="info"   >
       Preuzmi izvjestaj
        </Button>
        </Form>
</div>
</Modal>
 
 
 
 
      <Modal  modalClassName="modal-register" isOpen={this.state.showAddType}>
<div className="modal-header no-border-header text-center">
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({showAddType:false, tipName:"",messageType:""} )} // na izlaz
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body">
          <Form onSubmit={this.addType} >
        <FormGroup>
        <label>Naziv tipa</label>
        <Input  name="tip"  value = {this.state.tipPregleda}  onChange={(event) => this.typeValidation(event)} type="text"  />
        <br></br>
        <label>Cijena pregleda ovog tipa</label>
        <Input  name="cijena"  value = {this.state.cijenaTipa}  onChange={(event) => this.cijenaTipaValidacija(event)} type="number"  />
        <p style={{color:'red'}} > {this.state.messageType} </p>
        <p style={{color:'red'}} > {this.state.message1Type} </p>
        </FormGroup>
        <Button block className="btn-round" color="info"   >
       Dodaj
        </Button>
        </Form>
</div>
</Modal>
 
     <Modal modalClassName="modal-register" isOpen={this.state.temp1}>
      <div className="modal-header no-border-header text-center">
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({temp1:false,name1:'',description1:'', city1:'',address1:''})} // na izlaz
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body">    
          <Form onSubmit={this.updateOneClinic}>
                  <FormGroup>
                  <label>Naziv</label>
                  <Input name="name1"  value={this.state.name1}  onChange={(event) => this.validacija(event)} type="text"  />
                  <p style={{color:'red'}} > {this.state.nameValidation} </p>
                  </FormGroup>
                  <FormGroup>
                  <label>Opis:</label>
                  <Input name="description" type="textarea" value={this.state.description1} onChange={(event) => this.validacija1(event)} />
                  <p style={{ color: 'red' }}>{this.state.descriptionValidation}</p>
                  </FormGroup>
                    <FormGroup>
                  <label>Grad</label>
                  <Input name="city1" value = {this.state.city1} onChange={(event) => this.validacija2(event)} type="text" />
                  <p style={{color:'red'}} > {this.state.city1Validation} </p>
                  </FormGroup>  
                  <FormGroup>
                  <label>Adresa</label>
                  <Input name="address1" value = {this.state.address1} onChange={(event) => this.validacija3(event)} type="text" />
                  <p style={{color:'red'}} > {this.state.address1Validation} </p>
                  </FormGroup>          
                  <Button block className="btn-round" color="info"  >
                    Izmijeni podatke
                  </Button>
                 
                  <p style={{color:'red'}} > {this.state.message} </p>
                  </Form>        
             </div>
    </Modal>
 
 
 
 
 
 
 
    <Col className="ml-auto mr-auto" md="6">
          <Form>
                  <FormGroup>
                  <label>Naziv</label>
                  <Input name="name" disabled={this.state.temp} value = {this.state.name} type="text"  />
                  </FormGroup>
                  <FormGroup>
                  <label>Grad</label>
                  <Input name="city" disabled={this.state.temp} value = {this.state.city} type="text" />
                  </FormGroup>
                  <FormGroup>
                  <label>Adresa</label>
                  <Input name="city" disabled={this.state.temp} value = {this.state.address} type="text" />
                  </FormGroup>
                  <FormGroup>
                  <label>Rejting</label>
                  <Input name="city1" value = {this.state.raiting} disabled={this.state.temp}  type="text" />
                  </FormGroup>
                  <FormGroup>
                  <label>Tipovi pregleda:  &nbsp;&nbsp;&nbsp;</label>
                  <section className="bar pt-0" hidden={this.state.tipovi.length === 0}>
                    <div className="row">
                            <div  className="col-md-12">
                                <div className="box mt-0 mb-lg-0">
                                    <div className="table-responsive">
                                        <table className="table table-hover">
                                        <thead>
                                            <tr>
                                            <th className="text-primary font-weight-bold">Naziv</th>
                                            <th className="text-primary font-weight-bold">Cijena</th>
                                            <th className="text-primary font-weight-bold"></th>
                                            </tr>
                                        </thead>
                                        <tbody>  
                                        {this.state.tipovi.map(tip => (
                                            <tr key={tip.name}>
                                                <td>{tip.name}</td>
                                                <td>{tip.typePrice}</td>
                                                <td><button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={(e) => this.deleteType(tip.name, e)}>
                                                <span aria-hidden={true}>×</span></button></td>
                                            </tr>
                                            ))}
                                        </tbody>
                                        </table>
                                        <p style={{color:'red'}}>{this.state.roomVal}</p>
                                    </div>
                                </div>
                            </div>    
                        </div>
                    </section>
                    <Button className="btn-round" color="info" outline onClick={event => this.setState({showAddType: true})}>
                    <i className="fa fa-cog" /> Novi tip pregleda
                  </Button>
                <br></br>
                <br></br>
                  <label>Sale:  &nbsp;&nbsp;&nbsp;</label>
                    <section className="bar pt-0" hidden={this.state.rooms.length === 0}>
                    <div className="row">
                            <div  className="col-md-12">
                                <div className="box mt-0 mb-lg-0">
                                    <div className="table-responsive">
                                        <table className="table table-hover">
                                        <thead>
                                            <tr>
                                            <th className="text-primary font-weight-bold">Naziv</th>
                                            <th className="text-primary font-weight-bold">Broj</th>
                                            <th className="text-primary font-weight-bold">Tip</th>
                                            <th className="text-primary font-weight-bold">Tip</th>
                                            </tr>
                                        </thead>
                                        <tbody>  
                                        {this.state.rooms.map(room => (
                                            <tr key={room.name}>
                                                <td>{room.name}</td>
                                                <td>{room.number}</td>
                                                <td>{room.typeRoom}</td>
                                                <td><button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={(e) => this.deleteRoom(room.name, e)}>
                                                <span aria-hidden={true}>×</span></button></td>
                                            </tr>
                                            ))}
                                        </tbody>
                                        </table>
                                        <p style={{color:'red'}}>{this.state.roomVal}</p>
                                    </div>
                                </div>
                            </div>    
                        </div>
                    </section>
                  <Button className="btn-round" color="info" outline onClick={event => this.setState({newRoomShow: true})}>
                    <i className="fa fa-cog" /> Nova sala
                  </Button>
                  </FormGroup>
                  <Button block className="btn-round" color="info"  onClick={event => this.setState({temp1:true,name1:this.state.name,description1:this.state.description, city1:this.state.city,address1:this.state.address})}    >
                    Izmijeni podatke
                  </Button>
 
                  <br></br>
                 
                  <br></br>
                  <Button block className="btn-round" color="info"  onClick={event => this.setState({showPrihodi:true})}    >
                   Prihodi klinike
                  </Button>
 
                  <br></br>
                  <br></br>
                  <div>
                    <h2>Izvjestaj o pregledima </h2>
                        <Line data={this.state.pom1} />                
                  </div>
 
                    <div>
                 
                        <Line data={this.state.pom2} />                
                  </div>
 
                 
                <br></br>
                  <div>
                    <MapGoogle name = {this.state.name} address = {this.state.address}  city = {this.state.city} state = {this.state.drzava}> </MapGoogle>
                    </div>
                    <br></br>
                <br></br>
                <br></br>
                <br></br>
     
                  </Form>        
             </Col>
 
 
 
         
 </Container>
 
<NotificationContainer/>
      </div>
 
    </>
    )};
}
export default ViewAndEditClinic;