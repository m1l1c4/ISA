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
  Label,
  Form,
  FormGroup,
  Input,
  Modal,
  Container,
  Col
} from "reactstrap";

// core components
import ExamplesNavbar from 'components/Navbars/ExamplesNavbar.js';
import ProfilePageHeader from 'components/Headers/ProfilePageHeader.js';

class CodebookPage extends Component {
    constructor(props)
    {
      super(props);
      this.state = {
        codebook: [],
        newCode: false,
        code_type: "LIJEK",
        name: "",
        code: "",
        type: "LIJEK",
        nameVal: "",
        codeVal: ""
      };

      this.addCode = this.addCode.bind(this);
    }
    deleteCode(code, e){
        axios({
            method: 'post',
            url: 'http://localhost:8099/codebook/' + code,
            }).then((response) => {
                console.log(response);
                const items = this.state.codebook.filter(item => item.code !== code);
                this.setState({ codebook: items });
            }, (error) => {
            console.log(error);
            });

    };

    doc = document.documentElement.classList.remove("nav-open");

    codeNameValidation(e){
        this.setState({name:e.target.value})
        let name = e.target.value;
        if (name === undefined || name === '')
            this.setState({nameVal:"Naziv je obavezno polje."})
        else
            this.setState({nameVal:""})
    };

    codeValidation(e){
        this.setState({code:e.target.value})
        let code = e.target.value;

        if (code === undefined || code === ''){
            this.setState({codeVal:"Sifra je obavezno polje"})
        }
        else if (code.length <= 2 || code.length > 6){
            this.setState({codeVal:"Sifra mora imati od 2 do 6 karaktera."})
        }
        else
            this.setState({codeVal:""})
    };

    addCode = event => {
        event.preventDefault();

        let name = this.state.name;
        let code = this.state.code;
        let ok = true;

        if (code === undefined || code === ''){
            this.setState({codeVal:"Sifra je obavezno polje"})
            ok = false;
        }
        else if (code.length <= 2 || code.length > 6){
            this.setState({codeVal:"Sifra mora imati od 2 do 6 karaktera."})
            ok = false;
        }
        else
            this.setState({codeVal:""})
        
        if (name === undefined || name === ''){
            this.setState({nameVal:"Naziv je obavezno polje"})
            ok = false;
        }
        else
            this.setState({roomNameVal:""})
        
        if(ok){
            let data = {
            "name": this.state.name ,
            "code": this.state.code ,
            "type": this.state.type ,
            };        
            axios({
                method: 'post',
                url: 'http://localhost:8099/codebook',
                data: data,
                ContentType: 'application/json'
                }).then((response) => {

                    if(this.state.type === 'DIJAGNOZA')  NotificationManager.success('Uspjesno dodavanje dijagnoza!', 'Uspjesno!', 3000);
                    if(this.state.type === 'LIJEK')  NotificationManager.success('Uspjesno dodavanje lijeka!', 'Uspjesno!', 3000);

                   
                    console.log(response);
                    this.setState({name:"", code:"", type:"LIJEK"})
                    this.state.codebook.push(data)
                    this.setState({newCode: false});
                    this.setState({codeVal:""})
                    this.setState({nameVal:""})
                }, (error) => {
                console.log(error);
                    NotificationManager.error('Sifra mora biti jedinstvena', 'Greska!', 3000);
                this.setState({codeVal: "Šifra mora biti jedinstvena."})
                });
        }
  };

  handleOptionChange(changeEvent) {
    this.setState({
      type: changeEvent.target.value
    });
  };
  
  componentDidMount(){
    axios({
        method: 'get',
        url: 'http://localhost:8099/codebook',       
    }).then((response) => {
        console.log(response);
        this.setState({codebook:response.data})
    }, (error) => {
        console.log(error);
    });
}
showProfile(e){
    this.props.history.push('/administrator-page');
  }

  logoutUser = () => {  
    localStorage.removeItem('ulogovan')
    localStorage.removeItem('role')
    this.props.history.push('/register-page');
  }

  redirect = () => {
    this.props.history.push('/register-page');
  }

  showClinicPage = () =>{
    this.props.history.push('/clinic-page');
  }

  showRegistrationRequests = () =>{
    this.props.history.push('/registration-request');
  }

  render() {
    return (
      <>
        
        <ExamplesNavbar logoutEvent={this.logoutUser} showProfileEvent={() => this.showProfile()}
                      hideKalendar={true}
                      hideNewWorker = {true}
                      hideNewQuick = {true}
                      hideReceipts = {true}
                      hideTypeAdmin = {true}
                      hideCodebookAdmin = {true}
                      hideLoginEvent = {true}
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
                      sakrij = {true}
                      hideAllQuicksEvent = {true}                      
                      hideKarton = {true}
                      showClinicPage={()=> this.showClinicPage()}
                      showRegistrationRequests = {() => this.showRegistrationRequests()}
                      hideRegisterEvent = {true}
        />
        <ProfilePageHeader />
        <div className="section profile-content">
        <Container>
            <Col className="ml-auto mr-auto" md="6">
                <br></br>
                <h3>Šifarnik:</h3>
                <br></br>
                <Button className="btn-round" color="default" style ={{"margin-right":30}} outline onClick={event => this.setState({code_type: "LIJEK"})}>
                    <i className="fa fa-cog" /> Lekovi
                </Button>   
                <Button className="btn-round" color="default" outline onClick={event => this.setState({code_type: "DIJAGNOZA"})}>
                    <i className="fa fa-cog" /> Dijagnoze
                </Button>   
                <br></br>
                <Modal modalClassName="modal-register" isOpen={this.state.newCode}>
      
                <div className="modal-header no-border-header text-center">
                    <button
                        aria-label="Close"
                        className="close"
                        data-dismiss="modal"
                        type="button"
                        onClick={() => this.setState({newCode: false, codeVal:"", nameVal:""})}
                    >
                    <span aria-hidden={true}>×</span>
                    </button>
                    <h3 className="title mx-auto">Nova sifra</h3>
                </div>
                <div className="modal-body">                       
                        <Form onSubmit={this.addCode}>
                            <FormGroup>
                            <label>Naziv</label>
                            <Input name="name" type="text" onChange={(event) => this.codeNameValidation(event)} />
                            <p style={{color:'red'}}>{this.state.nameVal}</p>
                            </FormGroup>
                            <FormGroup>
                            <label>Šifra</label>
                            <Input name="code" type="text" onChange={(event) => this.codeValidation(event)} />
                            <p style={{color:'red'}}>{this.state.codeVal}</p>
                            <label>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <Input name="type" type="radio" value="LIJEK" checked={this.state.type==='LIJEK'} onChange={(event) => this.handleOptionChange(event)}></Input>
                                LIJEK
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </label>
                            <label>
                                <Input name="type" type="radio" value="DIJAGNOZA" checked={this.state.type==='DIJAGNOZA'} onChange={(event) => this.handleOptionChange(event)}></Input>
                                DIJAGNOZA
                            </label>
                            </FormGroup>           
                            <br></br>
                            <Button block className="btn-round" color="info">
                                 Dodaj sifru
                            </Button>
                            </Form>
                </div>
                </Modal>
                <Form onSubmit={this.addClinic}>
                    <Label  name="codebooks" type="text" /> 

                    <section className="bar pt-0">
                    <div className="row">
                            <div  className="col-md-12">
                                <div className="box mt-0 mb-lg-0">
                                    <div className="table-responsive">
                                        <table className="table table-hover">
                                        <thead>
                                            <tr>
                                            <th className="text-primary font-weight-bold">Šifra</th>
                                            <th className="text-primary font-weight-bold">Naziv</th>
                                            <th className="text-primary font-weight-bold"></th>
                                            </tr>
                                        </thead>
                                        <tbody>  
                                        {this.state.codebook.map(code => (
                                            <tr key={code.code} hidden={this.state.code_type !== code.type}>
                                                <td>{code.code}</td>
                                                <td>{code.name}</td>
                                                <td><button aria-label="Close" className="close" data-dismiss="modal" type="button" 
                                                        onClick={(e) => this.deleteCode(code.code, e)}>
                                                        <span aria-hidden={true}>×</span>
                                                    </button>
                                                </td>
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
                  <Button className="btn-round" color="info" outline onClick={event => this.setState({newCode: true})}>
                    <i className="fa fa-cog" /> Nova sifra
                  </Button>         
                  </Form>
            </Col>
            
        </Container>
        <NotificationContainer/>
      </div>
   
    </>
  )};
}

export default CodebookPage;
