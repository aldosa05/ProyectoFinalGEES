package com.example.proyectofinal;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/usuarios/login")
    Call<Usuario> login(@Body Usuario usuario);

    // Usamos Usuario como respuesta
    @PUT("api/usuarios/cambiarContrasena")
    Call<ResponseBody> cambiarContrasena(@Body CambioContrasenaRequest request);
    @POST("api/usuarios/registro")
    Call<Void> registrarUsuario(@Body Usuario usuario);

    @GET("api/usuariosEquipos/usuario/{idUsuario}")
    Call<List<UsuarioEquipo>> getMisEquipos(@Path("idUsuario") int idUsuario);

    @GET("api/usuariosEquipos/usuario/{idUsuario}/equiposInfo")
    Call<List<UsuarioEquipoDTO>> getEquiposInfo(@Path("idUsuario") int idUsuario);

}
