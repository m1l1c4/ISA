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
  Col,
  Form,
  Modal,
  Label
} from "reactstrap";

// core components
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';
const url = 'https://clinical-center-tim31.herokuapp.com/'

class MedicalWorkerPage extends Component {
  constructor(props)
  {
    super(props);

    this.state = {
      dataShow: false,
      name: "" ,
      surname: "" ,
      email: "" ,
      phone: "" ,
      clinic: "" ,
      raiting: "" ,
      workingTime: "" ,
      temp:true,
      temp1:false,
      password:"",
      name1:"",
      surname1:"",
      phone1:"",
      password1:"",
      password11:"",
      message:"",
      nameValidation:"",
      surnameValidation:"",
      phoneValidation:"",
      password1Validation:"",
      password11Validation:"",
      flag1: true,
      flag2: true,
      flag3: true,
      flag4: true,
      flag5: true,
      changePass: false,
      message1:"",
      showRecipes: false,
      recipes: [],
      imePacijenta:"",
      prezimePacijenta:"",
      JBOPacijenta:"",
      pacijenti:[],
      showFiltriranjePacijenta: true,
      brojJBOFiltriranje: "",
      showNewAbsence: false,
      absenceStart: new Date(),
      absenceEnd: new Date(),
      typeOfAbsence:"ODMOR",
      showListOfDoctors: true,
      sort1: true,
      sort2: true,
      sort3:true,
    };

    this.updateOneWorker = this.updateOneWorker.bind(this);  
    this.cancelSearchPatients = this.cancelSearchPatients.bind(this);
    this.traziPacijente = this.traziPacijente.bind(this); 
    this.traziPoJBO = this.traziPoJBO.bind(this);
   // this.sortByJBO = this.sortByJBO(this);
    //this.sortByPrezime = this.sortByPrezime(this);
    this.pristupiPacijentu = this.pristupiPacijentu.bind(this);
    this.createAbsenceRequest = this.createAbsenceRequest.bind(this);
  }
      
  doc = document.documentElement.classList.remove("nav-open");
 
  //React.useEffect(() => {
    //document.body.classList.add("landing-page");
    //return function cleanup() {
      //document.body.classList.remove("landing-page");
   // };
 // });


 componentDidMount(){
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  let today = new Date();
  var t = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
  this.setState({absenceStart:t, absenceEnd:t})
  axios({
    method: 'get',
    url:url + 'getMedicalWorker',
    headers: { "Authorization": AuthStr }   
  }).then((response)=>{
    console.log(response);
    let pom  = response.data.startHr + ' - ' + response.data.endHr;
    this.setState({name:response.data.user.name, raiting:response.data.rating, workingTime:pom, clinic:response.data.clinic, email:response.data.user.email,surname:response.data.user.surname,phone:response.data.phone,password:response.data.user.password})
  },(error)=>{
    console.log(error);
  });

    axios({
      method: 'get',
      url: url + 'getPatients',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({pacijenti:response.data})
    }, (error) => {
      console.log(error);
    });


 }

 redirect = () => {
  this.props.history.push('/register-page');
}

logoutUser = () => {  
  localStorage.removeItem('ulogovan')
  localStorage.removeItem('role')
  this.props.history.push('/register-page');
}


pristupiPacijentu(emailP, id, e){

 this.props.history.push('/patient-page/' + id); // id usera od pacijenta

 // ovo treba na sljedecoj strani da vidimo da li moze pristupiti pacijentovom kartonu
 let token = localStorage.getItem("ulogovan")
 let AuthStr = 'Bearer '.concat(token);
 axios({
   method: 'get',
   url: url + 'canAccessToMedicalRecord',
   data: emailP,
   headers: { "Authorization": AuthStr }  ,
   ContentType: 'application/json',
 }).then((response) => {
   console.log(response);
     if(response.data === "DA"){
      // 
     }
     else{
      NotificationManager.info('Ne mozete pristupiti pacijentu!', 'Info!', 3000);
       this.setState({obavjestenje:"Ne mozete pristupiti pacijentu"});
     }
 }, (error) => {
   console.log(error);
 });
  
}

traziPacijente = event => {
event.preventDefault();
let token = localStorage.getItem("ulogovan")
let AuthStr = 'Bearer '.concat(token);


  let ime = this.state.imePacijenta
  let prezime = this.state.prezimePacijenta
  let brJBO = this.state.JBOPacijenta;

  if(ime === undefined) {
    this.setState({imePacijenta:""})
    ime = ""
    }
if(prezime=== undefined) {
  this.setState({prezimePacijenta:""})
  prezime = "";
  }
if(brJBO=== undefined) {
  this.setState({JBOPacijenta:""})
  brJBO = "";
 } /* if (parametar === "")
    this.setState({messageDoktor:"Polje za pretragu je prazno!"})*/
 // else {
    let params = [];
    params.push(ime);
    params.push(prezime);
    params.push(brJBO);

    let ok = true; 
    if(ime === "" &&  prezime === "" && brJBO === "" ){
      ok = false;
      this.setState({messagePacijent: "Morate unijeti neke podatke za pretragu!"})
    }
 
    if(ok){
      axios({
        method: 'post',
        url: url + 'findPatients',
        headers: { "Authorization": AuthStr } ,
        data: params,
        ContentType: 'application/json',
      }).then((response)=>{      
        this.setState({pacijenti: response.data}) ;
        this.setState({messagePacijent:"" , showFiltriranjePacijenta:false})
      },(error)=>{
        console.log(error);
        this.setState({messagePacijent: "Ne postoji pacijent "})
      });
    }

}
 
sortByJBO  () {
  let temp = this.state.pacijenti;
  if(this.state.sort1){
    temp.sort(function(a,b){let jbo1 = a.jbo; let jbo2 = b.jbo; return jbo2.localeCompare(jbo1)})
  }
  else{
    temp.sort(function(a,b){let jbo1 = a.jbo; let jbo2 = b.jbo; return jbo1.localeCompare(jbo2)})
  }
 
  this.setState({pacijenti:temp, sort1: !this.state.sort1})
}

 
sortByPrezime(){
  let temp = this.state.pacijenti;
  if(this.state.sort2){
    temp.sort(function(a,b){let prz1 = a.user.surname; let prz2 = b.user.surname; return prz2.localeCompare(prz1)})
  }
else{
  temp.sort(function(a,b){let prz1 = a.user.surname; let prz2 = b.user.surname; return prz1.localeCompare(prz2)})
}
  this.setState({pacijenti:temp,sort2: !this.state.sort2})
}

sortByIme(){
  let temp = this.state.pacijenti;
  if(this.state.sort3){
    temp.sort(function(a,b){let ime1 = a.user.name; let ime2 = b.user.name; return ime2.localeCompare(ime1)})
  }
else{
  temp.sort(function(a,b){let ime1 = a.user.name; let ime2 = b.user.name; return ime1.localeCompare(ime2)})
}
  this.setState({pacijenti:temp,sort3: !this.state.sort3})
}


traziPoJBO = () => {
  let pom = [];
  pom.push(this.state.brojJBOFiltriranje);
  alert(pom)
  let ok = true;
  if(pom === undefined || pom === ""){
    ok = false;
  }
  if(ok){
    axios({
      method: 'post',
      url: url + 'filterPatients' ,
      data:pom,
      ContentType: 'application/json',
    }).then((response)=>{      
      if(response.status === 502)
      this.setState({messageFilterSale: "Ne postoji sa unesenim JBO"})
  else {
      this.setState({pacijenti: response.data}) ;
      this.setState({ showFiltriranjePacijenta:false})
      }
  
    },(error)=>{
      console.log(error);
    });
  }
 
}


cancelSearchPatients = () => {
  this.setState({messagePacijent:""})
    this.setState({imePacijenta:""})
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    axios({
      method: 'get',
      url: url + 'getPatients',
      headers: { "Authorization": AuthStr }  
    }).then((response) => {
      console.log(response);
        this.setState({pacijenti:response.data, imePacijenta:"",prezimePacijenta:"", JBOPacijenta:"",showFiltriranjePacijenta:true })
    }, (error) => {
      console.log(error);
    });

}

 updateOneWorker = event => {
    event.preventDefault();
    let isOk = true;

      let nameMy = this.state.name1
      let surnameMy = this.state.surname1;
      let phoneMy = this.state.phone1

      if (nameMy === undefined || nameMy === ''){
        this.setState({nameValidation:"Ime je obavezno polje."})
        isOk = false;
      }
      else if(!nameMy[0].match('[A-Z]')){
        this.setState({nameValidation:"Ime mora pocinjati velikim slovom."})
        isOk = false;
     }
      else{
        this.setState({nameValidation:""})
      }

      if (surnameMy === undefined || surnameMy === ''){
        this.setState({surnameValidation:"Prezime je obavezno polje."})
        isOk = false;
      }
      else if(!surnameMy[0].match('[A-Z]')){
        this.setState({surnameValidation:"Prezime mora pocinjati velikim slovom."})
        isOk = false;
     }
      else{
        this.setState({surnameValidation:""})
      }

      if (phoneMy === undefined || phoneMy === ''){
        this.setState({phoneValidation:"Telefon je obavezno polje."})
        isOk = false;
      }
      else{
        this.setState({phoneValidation:""})
      }
      

  if(isOk){

    this.setState({name:this.state.name1, surname:this.state.surname1, phone:this.state.phone1})
        let data = {
        "user":{
          "name": this.state.name1 ,
          "surname": this.state.surname1 ,
          "email": this.state.email ,
          "password": this.state.password
        },
        "phone": this.state.phone1 ,
      };


    axios({
      method: 'post',
      url: url + 'updateMedicalWorker',
      data: data
    }).then((response) => {
      console.log(response);
      NotificationManager.success('Izmjene su uspjesno obavljene!', 'Uspjesno!', 3000);
      this.setState({temp1: false});
    }, (error) => {
      console.log(error);
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


validacija1(e) {
  this.setState({surname1 : e.target.value});
  let sun = e.target.value;
  if(sun===''){
    this.setState({surnameValidation : "Ovo polje ne moze biti prazno", flag2:false});
  }
  else if(!sun[0].match('[A-Z]')){
    this.setState({surnameValidation : "Prezime mora da pocinje velikim slovom", flag2:false});
  }
  else{
    this.setState({surnameValidation : "", flag2:true});
  }

  e.preventDefault();
}

validacija2(e) {
  this.setState({phone1 : e.target.value});
  let sun = e.target.value;
  if(sun===''){
    this.setState({phoneValidation : "Ovo polje ne moze biti prazno", flag3:false});
  }
  else if(sun!==''){
    this.setState({phoneValidation : "", flag3:false});
  }
  else{
    this.setState({phoneValidation : "", flag3:true});
  }
  e.preventDefault();
}


validacija3(e) {
  this.setState({password1 : e.target.value});
  let sun = e.target.value;
  if(sun===''){
    this.setState({password1Validation : "Ovo polje ne moze biti prazno", flag4:false});
  }
  else if(sun!==''){
    this.setState({password1Validation : "", flag4:false});
  }
  else{
    this.setState({password1Validation : "", flag4:true});
  }

  e.preventDefault();
}


validacija4(e) {
  this.setState({password11 : e.target.value});
  let sun = e.target.value;
  if(sun===''){
    this.setState({password11Validation : "Ovo polje ne moze biti prazno", flag5:false});
  }
  else if(sun!==undefined && this.state.password1!==e.target.value){
    this.setState({password11Validation: "Lozinke se moraju poklapati", flag5:false})
  }
  else if(sun === this.state.password1) {
    this.setState({password11Validation: "", flag5:true})
  }
  else{
    this.setState({password11Validation : "", flag5:true});
  }

  e.preventDefault();
}


pacijentiEventShow = (e) =>{
  this.setState({showListOfDoctors:false})
 
}

changePassword = event => {
  event.preventDefault();
  this.setState({changeData:true})
  let pom1 = this.state.password1;
  let pom2 = this.state.password11;
  let isOk = true;
  if(pom1!==pom2){
      this.setState({message1 : "Lozinke se moraju poklapati!"});
      isOk = false;
  }
  else if(pom1 === undefined || pom1 === ""){
    this.setState({message1 : "Lozinke se moraju poklapati!"});
    isOk = false;
  }
  else{
    this.setState({changePass:false, password:this.state.password1, password1:"", password11:""});
  }

  if(isOk){
    this.setState({name:this.state.name1, surname:this.state.surname1, phone:this.state.phone1})
    let token = localStorage.getItem("ulogovan")
    let AuthStr = 'Bearer '.concat(token);
    let data = [];
    data.push(this.state.password1);

    axios({
      method: 'post',
      url: url + 'changePassword',
      data: data,
      headers: { "Authorization": AuthStr } ,
      ContentType: 'application/json'
    }).then((response) => {
      console.log(response);
      NotificationManager.success('Uspjesna izmjena lozinke!', 'Uspjesno!', 3000);
      this.setState({ changeData: false })
    }, (error) => {
      console.log(error);
    });

    this.setState({ changeData: false })
  }
}

loadRecipes(){
  this.setState({showRecipes:true})
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  axios({
        method: 'get',
        url:url + 'getRecipes',    
        headers: { "Authorization": AuthStr }           
    }).then((response) => {
        console.log(response);
        this.setState({recipes:response.data})
    }, (error) => {
        console.log(error);
  });
}

verify(id, e){
  e.preventDefault();
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  axios({
      method: 'post',
      url: url + 'verifyRecipe/' + id ,
      headers: { "Authorization": AuthStr },
  }).then((response) => {
      console.log(response);
      //alert("Recept je uspjesno ovjeren");
      NotificationManager.success('Recept je uspjesno ovjeren!', 'Uspjesno!', 3000);
      this.deleteRecipe(id);
  }, (error) => {
      console.log(error);
      NotificationManager.error('Doslo je do greske!', 'Greska!', 3000);
  });

}

createAbsenceRequest = event => {
  event.preventDefault()
  let token = localStorage.getItem("ulogovan")
  let AuthStr = 'Bearer '.concat(token);
  let data ={
    "startVacation" : this.state.absenceStart,
    "endVacation" : this.state.absenceEnd,
    "typeOfAbsence" : this.state.typeOfAbsence,
  }
  axios({
      method: 'post',
      url: url + 'vacationRequest',
      headers: { "Authorization": AuthStr },
      data: data
  }).then((response) => {
      console.log(response);
     // alert("Zahtjev za odmor uspjesno poslat");
     NotificationManager.success('Zahtjev za odmor uspjesno poslat!', 'Uspjesno!', 3000);
      this.setState({showNewAbsence:false, absenceStart: new Date(), absenceEnd: new Date(), absenceType:"ODMOR"})
  }, (error) => {
      console.log(error);
      NotificationManager.error('Doslo je do greske!', 'Greska!', 3000);
     // alert("Doslo je do greske");
  });
}

handleOptionChange(changeEvent) {
  this.setState({
    typeOfAbsence: changeEvent.target.value
  });
}

deleteRecipe(id) {
  const items = this.state.recipes.filter(recipe => recipe.id !== id);
  this.setState({ recipes: items });
};

showCheckup(e){
  this.props.history.push('/checkup/1');
}

render() {
  return (
    <>
      <ExamplesNavbar showCheckup = {() => this.showCheckup()}
                      recipes={() => this.loadRecipes()}
                      logoutEvent={this.logoutUser}                      
                      pacijentiEvent = {() => this.pacijentiEventShow()}
                      absence = {() => this.setState({showNewAbsence:true})}
                      redirectKalendar = {() => this.props.history.push('/calendar')}
                      hideNewWorker = {true}
                      hideNewQuick = {true}
                      hideTypeAdmin = {true}
                      hideCodebookAdmin = {true}
                      hideRequestsAdmin = {true}
                      hideLoginEvent = {true}
                      hidePatientKlinike = {true}
                      hideCheckupDoctor = {true}
                      hideRoomsAdmin = {true}
                      hideDocsAdmin = {true}
                      hideClinics = {true}
                      hideClinicInfoAdmin = {true}
                      hideAddNewClinic = {true}
                      hideRegisterEvent = {true}
                                            
                      sakrij = {true}
                      hideAllQuicksEvent = {true}                      
                      hideKarton = {true}
                      hidePregledi = {true}
                      />
      <ProfilePageHeader />
      <div hidden = {!this.state.showRecipes}>
        <div className="section profile-content">
          <div id="wrapper">
            <section class="section lb">
              <div class="container">
                <div class="section-title text-center">
                  <h3 hidden={this.state.recipes.length === 0}>Pregled pristiglih recepata za overu</h3>
                  <h3 hidden={this.state.recipes.length > 0}>Trenutno nema novih recepata</h3>
                  <br></br>
                  <br></br>
                </div>
                <ul className="list-group">
                  {this.state.recipes.map(recipe => (
                    <li key={recipe.id}>
                      <div class="col-lg-12 col-md-12">
                        <div class="blog-box" >
                          <div class="blog-desc">
                            <h4>{recipe.name} {recipe.surname}</h4>
                            <p>Doktor: {recipe.report.checkUp.medicalWorker.user.name} {recipe.report.checkUp.medicalWorker.user.surname}</p>
                            <p>Pacijent: {recipe.report.checkUp.patient.name} {recipe.report.checkUp.patient.surname} </p>
                            <p>Lijek: {recipe.code.name} {recipe.code.code}</p>
                            <button style ={{"margin-right":30, position: 'absolute', right: 0}} class="btn btn-primary" color="default" outline
                              onClick={(e) => this.verify(recipe.id, e)}>
                              Overi
                            </button>
                            <br></br>
                          </div>
                        </div>
                      </div>
                    </li>
                  ))}</ul>
                  </div>
                </section>
              </div>
            </div>
      </div>
      <div className="section profile-content" hidden={this.state.showRecipes}>
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
              <h4 className="title">
                {this.state.name} {this.state.surname}<br />
              </h4>
              <h6 className="description">Medicinski radnik</h6>
            </div>
          </div>
          <br />

    <Modal modalClassName="modal-register" isOpen={this.state.showNewAbsence}>
      <div className="modal-header no-border-header text-center">
          <button aria-label="Close" className="close" data-dismiss="modal" type="button" onClick={event => this.setState({showNewAbsence:false})}>
            <span aria-hidden={true}>x</span>
          </button>
      </div>
          <div className="modal-body">     
            <Form onSubmit={this.createAbsenceRequest}>
                  <FormGroup>
                    <Label>Tip:&nbsp;&nbsp;</Label>
                    <select value={this.state.typeOfAbsence}
                        onChange={(e) =>  this.handleOptionChange(e)}>
                        <option value={"ODMOR"}>Odmor</option>
                        <option value={"ODSUSTVO"}>Odsustvo</option>
                      </select>
                  </FormGroup>
                  <FormGroup>
                  <Label>Datum poèetka:</Label>
                  <Input type="date" className="form-control" value={this.state.absenceStart} onChange = {event => this.setState({absenceStart: event.target.value})}/>
                  </FormGroup>
                  <FormGroup>
                  <Label>Datum završetka:</Label>
                  <Input type="date" className="form-control" value={this.state.absenceEnd} onChange = {event => this.setState({absenceEnd: event.target.value})}/>
                  <p style={{color:'red'}} > {this.state.phoneValidation} </p>
                  </FormGroup>
                  <Button block className="btn-round" color="info">
                    Kreiraj zahtev
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
            onClick={event => this.setState({temp1:false,name1:'',surname1:'', phone1:'',password1:''})} // na izlaz
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body">     
          <Form onSubmit={this.updateOneWorker}>
                  <FormGroup>
                  <label>Ime</label>
                  <Input name="name1"  value={this.state.name1}  onChange={(event) => this.validacija(event)} type="text"  />
                  <p style={{color:'red'}} > {this.state.nameValidation} </p>
                  </FormGroup>
                  <FormGroup>
                  <label>Prezime</label>
                  <Input name="surname1" value = {this.state.surname1} onChange={(event) => this.validacija1(event)} type="text" />
                  <p style={{color:'red'}} > {this.state.surnameValidation} </p>
                  </FormGroup>
                  <FormGroup>
                  <label>Telefon</label>
                  <Input  name="phone1"  value = {this.state.phone1} onChange={(event) => this.validacija2(event)} type="number"  />
                  <p style={{color:'red'}} > {this.state.phoneValidation} </p>
                  </FormGroup>
                  <Button block className="btn-round" color="info"  onClick={event => this.setState({changePass: true})}  >

                Promijeni lozinku
                  </Button>

                  <Button block className="btn-round" color="info"  >
                    Izmijeni podatke
                  </Button>
                  <p style={{color:'red'}} > {this.state.message} </p>
                  </Form>         
             </div>
    </Modal>



    <Modal  modalClassName="modal-register" isOpen={this.state.changePass}>
<div className="modal-header no-border-header text-center">
          <button
            aria-label="Close"
            className="close"
            data-dismiss="modal"
            type="button"
            onClick={event => this.setState({changePass:false, password1:"",password11:""})} // na izlaz 
          >
          <span aria-hidden={true}>x</span>
          </button>
          </div>
          <div className="modal-body"> 
          <Form onSubmit={this.changePassword} > 
        <FormGroup>
        <label>Nova lozinka</label>
        <Input  name="password1"  value = {this.state.password1}  onChange={(event) => this.validacija3(event)} type="password"  />
        <p style={{color:'red'}} > {this.state.password1Validation} </p>
        </FormGroup>
        <FormGroup>
        <label>Potvrda lozinke</label>
        <Input  name="password11"  value = {this.state.password11} onChange={(event) => this.validacija4(event)}  type="password"  />
        <label color="red" name="password11Validation" > </label>
        <p style={{color:'red'}} > {this.state.message1} </p>
        </FormGroup>
        <Button block className="btn-round" color="info"   >
       Sacuvaj izmjene
        </Button>
        </Form>
</div>
</Modal>

<div > </div>

    <Col   hidden={!this.state.showListOfDoctors} className="ml-auto mr-auto" md="6"> 
          <Form>
                  <FormGroup>
                  <label>Ime</label>
                  <Input name="name" disabled={this.state.temp} value = {this.state.name} type="text"  />
                  </FormGroup>
                  <FormGroup>
                  <label>Prezime</label>
                  <Input name="surname" disabled={this.state.temp} value = {this.state.surname} type="text" />
                  </FormGroup>
                  <FormGroup>
                  <label>Email</label>
                  <Input name="email" disabled={this.state.temp} value = {this.state.email} type="text" />
                  </FormGroup>
                  <FormGroup>
                  <label>Telefon</label>
                  <Input  name="phone" disabled={this.state.temp} value = {this.state.phone} type="number"  />
                  </FormGroup>
                  <FormGroup>
                  <label>Klinika</label>
                  <Input  name="clinic"   disabled={this.state.temp} type="text" value = {this.state.clinic.name}/>
                  </FormGroup>
                  <FormGroup>
                  <label>Ocjena</label>
                  <Input name="raiting"  disabled={this.state.temp} type="number" value = {this.state.raiting}/>
                  </FormGroup>
                  <FormGroup>
                  <label>Radno vrijeme</label>
                  <Input  name="workingTime"  disabled={this.state.temp} type="text" value = {this.state.workingTime}/>
                  </FormGroup>                 
                  <Button block className="btn-round" color="info"  onClick={event => this.setState({temp1:true,name1:this.state.name,surname1:this.state.surname, phone1:this.state.phone,password1:""})}    >
                    Izmijeni podatke
                  </Button>
                  </Form>         
             </Col>


 <div hidden={this.state.showListOfDoctors} >
<p className="text lead">Pretraga pacijenata</p>
        <section className="bar pt-0">
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po imenu" value={this.state.imePacijenta} onChange={event => this.setState({imePacijenta: event.target.value })} />
              </div>
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po prezimenu" value={this.state.prezimePacijenta} onChange={event => this.setState({prezimePacijenta: event.target.value })} />
              </div>
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po JBO" value={this.state.JBOPacijenta} onChange={event => this.setState({JBOPacijenta: event.target.value })} />
              </div>
                {/*<div  className="col-sm-3">
                <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                            </div>  */}    
                
                             <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.traziPacijente}>Pretrazi </Button>
            </div>       
          
            <div  className="col-md-3">
        
                <Button block className="btn-round" color="info" onClick={this.cancelSearchPatients}>Poništi pretragu</Button>
            </div>         
            
        
          </div>
          <p style={{color:'red'}}>{this.state.messagePacijent}</p>
  </section>  
 
  <div hidden={this.state.showFiltriranjePacijenta} >
  <section className="bar pt-0">
          <div className="row">            
              <div  className="col-sm-3">
                  <Input name="filter[0]" type="text" placeholder="trazi po JBO" value={this.state.brojJBOFiltriranje} onChange={event => this.setState({brojJBOFiltriranje: event.target.value })} />
              </div>
                {/*<div  className="col-sm-3">
                <Input name="filter[0]" type="text" value={this.state.filter[0]} onChange={event => this.setState({filter: [...this.state.filter, event.target.value]})} />
                            </div>  */}               
            <div  className="col-md-3">    
                <Button block className="btn-round" color="info" onClick={this.traziPoJBO}>Trazi po broju  </Button>
            </div>
            <br></br><br></br>
          </div>
          <p style={{color:'red'}}>{this.state.messageFilterSale}</p>
  </section>  
                 
    
   </div>

        <section className="bar pt-0">
          <div className="row">
     <div  className="col-md-12">
         <br></br> <br></br>
            <br></br>
             <div className="box mt-0 mb-lg-0">
              <div className="table-responsive">
                <table className="table table-hover">
                  <thead>
                    <tr>
                      <th onClick={() => this.sortByIme()} className="text-primary font-weight-bold">Ime</th>
                      <th onClick={() => this.sortByPrezime()} className="text-primary font-weight-bold">Prezime</th>
                      <th onClick={() => this.sortByJBO()} className="text-primary font-weight-bold">JBO</th>
                   
                      {/*<th>Cena pregleda</th> */}              
                    </tr>
                  </thead>
                  <tbody>  
                  {this.state.pacijenti.map(p => (
                    <tr key={p.user.email}>
                        <td>{p.user.name} </td>
                        <td>{p.user.surname} </td>
                        <td>{p.jbo} </td>
   <td>      <Button block className="btn-round" color="info"  onClick={(e) => this.pristupiPacijentu(p.user.email, p.user.id ,e)}>Pristupi pacijentu</Button> </td>

                  
 
                    </tr>
                    ))}
                   
                  </tbody>
                </table>
              </div>
            </div>
          </div>    
    </div>
    </section>
    
<NotificationContainer/>
    </div>
















 </Container>
      </div>
 
    </>
    )};
}
export default MedicalWorkerPage;


