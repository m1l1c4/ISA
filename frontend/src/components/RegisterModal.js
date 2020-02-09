import React, {Component} from 'react';
// reactstrap components
import { Button, Form, Input, Modal } from "reactstrap";


class RegisterModal extends Component {


render() {

    return(
        <Modal modalClassName="modal-register" isOpen={this.props.open}>
        <div className="modal-header no-border-header text-center">
            <h3 className="title mx-auto">Dobrodošli!</h3>
        </div>
        <div className="modal-body">                        
                <Form className="register-form">
                  <label>JBO</label>
                  <Input placeholder="jedinstveni broj osiguranika" type="text" />
                  <label>Email</label>
                  <Input placeholder="email" type="text" />
                  <label>Ime</label>
                  <Input placeholder="ime" type="text" />
                  <label>Prezime</label>
                  <Input placeholder="prezime" type="text" />
                  <label>Lozinka</label>
                  <Input placeholder="lozinka" type="password" />
                  <label>Potvrdi lozinku</label>
                  <Input placeholder="lozinka" type="password" />
                  <label>Telefon</label>
                  <Input placeholder="telefon" type="text" />
                  <label>Adresa</label>
                  <Input placeholder="adresa" type="text" />
                  <label>Grad</label>
                  <Input placeholder="grad" type="text" />
                  <label>Država</label>
                  <Input placeholder="država" type="text" />
                  <Button block className="btn-round" color="info">
                    Registruj se
                  </Button>
                </Form>
                
        </div>
        </Modal>
    )
}












};

export default RegisterModal;