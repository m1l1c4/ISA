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
import "assets/css/bootstrap.min.css";
// reactstrap components
import { Button, Form, FormGroup, Input, Modal, FormText } from "reactstrap";
import { withRouter } from "react-router-dom";// core components


import {NotificationContainer, NotificationManager} from 'react-notifications';
import "../../../node_modules/react-notifications/lib/notifications.css"
import "../../../node_modules/react-notifications/lib/Notifications.js"
// core components
import ExamplesNavbar from "components/Navbars/ExamplesNavbar.js";

class RegisterPage extends Component {
  constructor(props)
  {
    super(props);
    this.state = {
      registerShow: false,
      loginShow: false,
      name: "" ,
      surname: "" ,
      email: "" ,
      jbo: "" ,
      password: "",
      passConf: "" ,
      phone: "" ,
      adress: "" ,
      city: "" ,
      statee: "" ,
      formValid: false  ,   // enable/disable submit button depending on form validation
      notFilledError: false ,
      formErrorText: "" ,
      passErrorText: "" ,
      emailErrorText: "" ,
      emailValid: true ,
      showResponse: false ,
      loginDisable: true ,
      message: "",
      changeFirstPass: false,
      goToProfilePage: false,
      password1: "",
      password11: "",
      password11Validation: "",
      password1Validation: ""
    };

    this.sendRegistration = this.sendRegistration.bind(this) ;
    this.validateEmptyFields = this.validateEmptyFields.bind(this) ;
    this.validateLoginFields = this.validateLoginFields.bind(this) ;
    this.handleMatching = this.handleMatching.bind(this) ;
    this.onDismiss = this.onDismiss.bind(this) ;
    this.cleanAll = this.cleanAll.bind(this) ;
    this.mailValidation = this.mailValidation.bind(this) ;
    this.sendLogin = this.sendLogin.bind(this) ;
    this.changePassword = this.changePassword.bind(this);
    this.getUlogovani = this.getUlogovani.bind(this) ;
    this.redirect = this.redirect.bind(this) ;
  }
      
  doc = document.documentElement.classList.remove("nav-open");
  /*eff = useEffect(() => {
    document.body.classList.add("register-page");
    return function cleanup() {
      document.body.classList.remove("register-page");
    };
  });*/

  validateEmptyFields = () => {     
      if (this.state.name === "" || this.state.surname === "" || this.state.email === "" || this.state.jbo === "" || this.state.phone === "" || this.state.adress === "" || this.state.city === "" || this.state.statee === "" )
      {
        this.setState({notFilledError:true});
      }
  }

  validateLoginFields = () => {     
    if (this.state.email === "" || this.state.password === "" )
    {
      this.setState({notFilledError:true});
    }
}

  onDismiss = () => this.setState({showResponse: !this.showResponse});

  cleanAll = () => this.setState({registerShow: false, name: "" , surname: "", email: "" , jbo: "", password: "",passConf: "" , phone: "" , adress: "" , city: "" , statee: "" , formValid: false  , notFilledError: false ,
                    formErrorText: "" , emailErrorText: "" , loginShow: false ,
                    passErrorText: ""})

  handleMatching = (event) => {
    if (this.state.password !== event.target.value)
      {
        this.setState({formValid: true, passErrorText: "Lozinke se ne poklapaju."});    // disabling submit button
      } else {
        this.setState({formValid: false, passErrorText: ""});
      }
  }

  mailValidation = (event) => {
    const regex = /\S+@\S+\.\S+/;
     if ( !regex.test(event.target.value) )   //email not appropriate
     {
        this.setState({formValid: false, loginDisable: true, emailErrorText: "Neispravan mejl. Ocekivan unos je u obliku local@domain."})
     } else {
      this.setState({formValid: true, emailErrorText: "", loginDisable: false})
     }
  }

  sendRegistration = event => {
        event.preventDefault();

        this.validateEmptyFields();
        
        if (this.state.notFilledError)
        {
            this.setState({formErrorText: "Sva polja su obavezna. Molim vas popunite prazna polja."})
        } else {

          let user = {
            "name": this.state.name ,
            "surname": this.state.surname ,
            "email": this.state.email ,
            "password":this.state.password,
            "enabled": true
          };

          let data = {
            "user": user,
            "jbo": this.state.jbo ,
            "phoneNumber": this.state.phone ,
            "address": this.state.adress ,
            "city": this.state.city ,
            "state": this.state.statee,
            "email": this.state.email ,
          };

          axios({
            method: 'post',
            url: 'http://localhost:8099/register',
            data: data ,
            ContentType: 'application/json'            
          }).then((response) => {
            if (response.status === 200)
            NotificationManager.success('Vas zahtev za registraciju je uspesno poslat!', 'Uspjesno!', 3000);
              this.setState({message: "Vas zahtev za registraciju je uspesno poslat." , showResponse: true}) ;
          }, (error) => {
            console.log(error);
          });

          this.cleanAll();
          
      }
  };

  redirect = (id) => {
    this.props.history.push('/patient-page/' + id);
  }

  getUlogovani = (token) => {
    let AuthStr = 'Bearer '.concat(token);

    axios({
      method: 'get' ,    
      url: 'http://localhost:8099/getUser' ,           
      headers: { "Authorization": AuthStr }   
    }).then((response) => {
      if (response.data != null)
      {
        if(response.data.type === "PACIJENT"){          
              this.redirect(response.data.id); 
        }
        else if(response.data.type === "CCADMIN" || response.data.type === "ADMINISTRATOR" || response.data.type === "DOKTOR" || response.data.type === "MEDICINAR"){
          if(!response.data.firstLogin){
            this.setState({changeFirstPass:true, password1:"", password11:""})
          }
          else{
            if(response.data.type === "DOKTOR" || response.data.type === "MEDICINAR")
              this.props.history.push('/medicalworker-page');
              else{
                this.props.history.push('/administrator-page');
              }
          }
        }
      }
    }, (error) => {
        this.setState({message: "Neuspesno dobavljanje korisnika.", showResponse: true}) ;
    });
  }

  changePassword = event =>{
    event.preventDefault();
    let isOk = true;
    let pass1 = this.state.password1;
    let pass2 = this.state.password11;
    if(pass1===''){
      this.setState({password1Validation : "Ovo polje ne moze biti prazno"});
      isOk = false;
    }
    else if(pass1!=='' || pass1===undefined){
      this.setState({password1Validation : ""});
      isOk = false;
    }
    else{
      this.setState({password1Validation : ""});
      isOk = true;
    }
  
    if(pass2===''){
      this.setState({password11Validation : "Ovo polje ne moze biti prazno"});
      isOk = false;
    }
    else if(pass2!==undefined && this.state.password1!==this.state.password11){
      this.setState({password11Validation: "Lozinke se moraju poklapati"})
      isOk = false;
    }
    else if(pass2 === this.state.password1) {
      this.setState({password11Validation: ""})
      isOk = true;
    }
    else{
      this.setState({password11Validation : ""});
      isOk = true;
    }
    if(isOk){
      this.setState({changeFirstPass : false});
      let text = []
      text.push(pass1)
      let token = localStorage.getItem("ulogovan")
      let AuthStr = 'Bearer '.concat(token);
      axios({
        method: 'post',
        url: 'http://localhost:8099/changePassword',
        data: text,
        headers: { "Authorization": AuthStr },
      }).then((response) => {
        console.log(response);
        NotificationManager.success('Uspjesna izmjena lozinke!', 'Uspjesno!', 3000);
        if(response.data.type === "DOKTOR" || response.data.type === "MEDICINAR")
        this.props.history.push('/medicalworker-page');
        else{
          this.props.history.push('/administrator-page');
        }
      }, (error) => {
        console.log(error);
      });
     }
  };

  

  pass1Validation(e) {
    this.setState({password1 : e.target.value});
    let sun = e.target.value;
    if(sun===''){
      this.setState({password1Validation : "Ovo polje ne moze biti prazno"});
    }
    else if(sun!==''){
      this.setState({password1Validation : ""});
    }
    else{
      this.setState({password1Validation : ""});
    }
  }
  
  pass2Validation(e) {
    this.setState({password11 : e.target.value});
    let sun = e.target.value;
    if(sun===''){
      this.setState({password11Validation : "Ovo polje ne moze biti prazno"});
    }
    else if(sun!==undefined && this.state.password1!==e.target.value){
      this.setState({password11Validation: "Lozinke se moraju poklapati"})
    }
    else if(sun === this.state.password1) {
      this.setState({password11Validation: ""})
    }
    else{
      this.setState({password11Validation : ""});
    }
  }

  getRole = () => {
    let AuthStr = 'Bearer '.concat(localStorage.getItem('ulogovan'))
      axios({
        method: 'get',
        url: 'http://localhost:8099/rollingInTheDeep' ,
        headers: { "Authorization": AuthStr }             
      }).then((response)=>{  
        if (response.status !== 404) {
          localStorage.setItem('role', response.data);
        }       
      },(error)=>{
        alert("GRESKAAA")
        console.log(error);
      });
  }

  sendLogin = event => {
    event.preventDefault();

    this.validateLoginFields();
    
    if (this.state.notFilledError)
    {
        this.setState({formErrorText: "Sva polja su obavezna. Molim vas popunite prazna polja."})
    } else {

      let data = {        
        "username": this.state.email ,        
        "password":this.state.password         
      };
      axios({
        method: 'post',
        url: 'http://localhost:8099/login',
        data: data,
        ContentType: 'application/json'
      }).then((response) => {
        if (response.status === 200)
        {
          localStorage.setItem("ulogovan", response.data.accessToken);
          this.getUlogovani(response.data.accessToken);
          this.getRole();
        }
          
      }, (error) => {
        NotificationManager.error('Neuspjesno logovanje!', 'Greska!', 3000);
          this.setState({message: "Neuspesno logovanje.", showResponse: true}) ;
      });

      this.cleanAll();
      
  }
};
  
  render() {
    return (
    <div>
      
      <ExamplesNavbar showRegister={() => this.setState({registerShow: true})} 
                      showLogin={() => this.setState({loginShow: true})}
                      hideKalendar={true}
                      hideProfilEvent = {true}
                      hideNewWorker = {true}
                      hideNewQuick = {true}
                      hideReceipts = {true}
                      hideTypeAdmin = {true}
                      hideCodebookAdmin = {true}
                      hideRequestsAdmin = {true}
                      hidePregledi = {true}
                      hidePatientKlinike = {true}
                      hideCheckupDoctor = {true}
                      hideRoomsAdmin = {true}
                      hideDocsAdmin = {true}
                      hideClinics = {true}
                      hideClinicInfoAdmin = {true}
                      hideAddNewClinic = {true}
                      hidePatientsDoc = {true}
                      hideVacation = {true}
                      hideLogOut = {true}
                      sakrij = {true}
                      hideAllQuicksEvent = {true}                      
                      hideKarton = {true}                      
                      />
      <div
        className="page-header"
        style={{
          backgroundImage: "url(" + require("assets/img/hc-complete-resize.jpg") + ")"
        }}
      >        
      </div>      
      <Modal modalClassName="modal-register" isOpen={this.state.registerShow}>      
        <div className="modal-header no-border-header text-center">
            <button
              aria-label="Close"
              className="close"
              data-dismiss="modal"
              type="button"
              onClick={() => this.cleanAll()}
            >
            <span aria-hidden={true}>×</span>
            </button>
            <h3 className="title mx-auto">Dobrodošli!</h3>
        </div>
        <div className="modal-body">                       
                <Form onSubmit={this.sendRegistration}>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">JBO</label>
                    <Input name="jbo" placeholder="jedinstveni broj osiguranika" type="text" value={this.state.jbo} onChange={event => this.setState({jbo: event.target.value})} />
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold"> Email</label>
                    <Input name="email" placeholder="email" type="text" value={this.state.email} onChange={event => this.setState({email: event.target.value})} onBlur={this.mailValidation}/>
                  <FormText color="danger">{this.state.emailErrorText}</FormText>
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Ime</label>
                    <Input name="name" placeholder="ime" type="text" value={this.state.name} onChange={event => this.setState({name: event.target.value})} />
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Prezime</label>
                    <Input  name="surname" placeholder="prezime" type="text" value={this.state.surname} onChange={event => this.setState({surname: event.target.value})} />
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Lozinka</label>
                    <Input  name="password" placeholder="lozinka" type="password" value={this.state.password} onChange={event => this.setState({password: event.target.value})} />
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Potvrdi lozinku</label>
                    <Input placeholder="lozinka" type="password" value={this.state.passConf} onBlur={this.handleMatching} onChange={event => this.setState({passConf: event.target.value})}/>
                  <FormText color="danger">{this.state.passErrorText}</FormText>
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Telefon</label>
                    <Input  name="phoneNumber" placeholder="telefon" type="text" value={this.state.phone} onChange={event => this.setState({phone: event.target.value})}/>
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Adresa</label>
                    <Input  name="adress" placeholder="adresa" type="text" value={this.state.adress} onChange={event => this.setState({adress: event.target.value})}/>
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Grad</label>
                    <Input  name="city" placeholder="grad" type="text" value={this.state.city} onChange={event => this.setState({city: event.target.value})} />
                  </FormGroup>
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Država</label>
                    <Input  name="statee" placeholder="država" type="text" value={this.state.statee} onChange={event => this.setState({statee: event.target.value})} />
                  </FormGroup>
                  <p className="text-danger font-weight-bold" >{this.state.formErrorText}</p>                  
                  <Button block className="btn-round" color="info" disabled={this.state.formValid}>
                    Registruj se
                  </Button>
                  </Form>
                
        </div>
        </Modal>

        <Modal modalClassName="modal-login" isOpen={this.state.changeFirstPass}>      
        <div className="modal-header no-border-header text-center">
            <h3 className="title mx-auto">Promjena lozinke</h3>
        </div>
        <div className="modal-body">                       
                <Form onSubmit={this.changePassword}>                  
                <FormGroup>
                <label>Nova lozinka</label>
                <Input  name="password1"  value = {this.state.password1}  onChange={(event) => this.pass1Validation(event)} type="password"  />
                <p style={{color:'red'}} > {this.state.password1Validation} </p>
                </FormGroup>
                <FormGroup>
                <label>Potvrda lozinke</label>
                <Input  name="password11"  value = {this.state.password11} onChange={(event) => this.pass2Validation(event)}  type="password"  />
                <label color="red" name="password11Validation" > </label>
                <p style={{color:'red'}} > {this.state.password11Validation} </p>
                </FormGroup>

                  <p className="text-danger font-weight-bold" >{this.state.formErrorText}</p>                  
                  <Button block className="btn-round" color="info" >
                    Promijeni lozinku
                  </Button>
                  </Form>
                
        </div>
        </Modal>
        
        <Modal modalClassName="modal-login" isOpen={this.state.loginShow}>      
        <div className="modal-header no-border-header text-center">
            <button
              aria-label="Close"
              className="close"
              data-dismiss="modal"
              type="button"
              onClick={() => this.cleanAll()}
            >
            <span aria-hidden={true}>×</span>
            </button>
            <h3 className="title mx-auto">Dobrodošli!</h3>
        </div>
        <div className="modal-body">                       
                <Form onSubmit={this.sendLogin}>                  
                  <FormGroup>
                    <label className="text-primary font-weight-bold"> Email</label>
                    <Input id = "loginEmail" className="form-control" name="email" placeholder="email" type="text" value={this.state.email} onChange={event => this.setState({email: event.target.value})} onBlur={this.mailValidation}/>
                  <FormText color="danger">{this.state.emailErrorText}</FormText>
                  </FormGroup>                  
                  <FormGroup>
                    <label className="text-primary font-weight-bold">Lozinka</label>
                    <Input id = "loginPassword"  name="password" className="form-control" placeholder="lozinka" type="password" value={this.state.password} onChange={event => this.setState({password: event.target.value})} />
                  </FormGroup>               
                  <p className="text-danger font-weight-bold" >{this.state.formErrorText}</p>                  
                  <Button id="confirmButton" block className="btn-round" color="info" disabled={this.state.loginDisable}>
                    Prijavi se
                  </Button>
                  </Form>
                
        </div>
        </Modal>
        <NotificationContainer/>
    </div>
    )};
}

export default withRouter(RegisterPage);
