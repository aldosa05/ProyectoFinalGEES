package com.example.proyectofinal;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {
    @POST("api/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);  // Usamos Usuario como respuesta

    @PUT("api/usuarios/cambiarContrasena")
    Call<ResponseBody> cambiarContrasena(@Body CambioContrasenaRequest request);
    @POST("api/usuarios/registro")
    Call<Void> registrarUsuario(@Body Usuario usuario);

}
