package com.taller.usuarioback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 12)
    private String rut;

    @Column(nullable = false)
    @NotBlank
    private String primerNombre;

    @Column
    private String segundoNombre;

    @Column(nullable = false)
    @NotBlank
    private String apellidoPaterno;

    @Column(nullable = false)
    @NotBlank
    private String apellidoMaterno;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String usuario;

    @Column(unique = true, nullable = false)
    @Email
    private String correo;

    @Column(nullable = false)
    @Size(min = 7, max = 18)
    private String password;

    @Column(name = "url_contrato")
    private String urlContrato;

    @Column(nullable = false)
    @NotBlank
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolUsuario rol;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoUsuario estado;

    public Usuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlContrato() {
        return urlContrato;
    }

    public void setUrlContrato(String urlContrato) {
        this.urlContrato = urlContrato;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
}
