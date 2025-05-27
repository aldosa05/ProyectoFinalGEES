package com.example.proyectofinal;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @DELETE("api/usuariosEquipos/eliminar/{idUsuario}/{idEquipo}")
    Call<Void> eliminarRelacionUsuarioEquipo(@Path("idUsuario") int idUsuario, @Path("idEquipo") int idEquipo);



    @POST("api/equipos/crear")
    Call<Equipo> crearEquipo(@Body CrearEquipoDTO crearEquipoDTO);

    @POST("api/equipos/unirse")
    Call<Void> unirseAEquipo(@Body UnirseEquipoDTO dto);

    @GET("api/horarios/equipo/{idEquipo}")
    Call<List<Horario>> getHorariosPorEquipo(@Path("idEquipo") int idEquipo);

    @DELETE("api/horarios/eliminar/{id}")
    Call<Void> eliminarHorario(@Path("id") int idHorario);


    @POST("api/horarios/crear")
    Call<Void> crearHorario(@Body Horario nuevoHorario);

    @POST("api/horarios/guardar")
    Call<Void> guardarHorarios(@Body List<Horario> horarios);

    @GET("api/convocatorias/ultima/{idEquipo}")
    Call<Convocatoria> getConvocatoriaPorEquipo(@Path("idEquipo") int idEquipo);


    @POST("api/convocatorias/crear")
    Call<Void> crearConvocatoria(@Body CrearConvocatoriaDTO dto);


    @GET("/api/usuarios-equipo/{idEquipo}")
    Call<List<Usuario>> obtenerJugadoresEquipo(@Path("idEquipo") int idEquipo);

    @POST("/api/convocados/{idPartido}")
    Call<Void> guardarConvocados(@Path("idPartido") int idPartido, @Body List<Integer> idsUsuarios);


    @GET("api/convocados/lista/{idPartido}")
    Call<List<Convocado>> getConvocadosPorPartido(@Path("idPartido") int idPartido);

    @POST("convocados")
    Call<Convocado> crearConvocado(@Body Convocado convocado);

    @DELETE("api/convocatorias/eliminar/{idPartido}")
    Call<Void> eliminarConvocatoria(@Path("idPartido") int idPartido);

    @POST("api/convocados/agregar")
    Call<Void> insertarConvocados(@Body List<ConvocadoRequest> convocados);

    @GET("api/coches")
    Call<List<CocheConOcupantesDTO>> obtenerCochesConOcupantes();

    @POST("api/coches/crear")
    Call<Coche> crearCoche(@Body Coche coche);

    @POST("api/coches/ocupante")
    Call<OcupanteCoche> anadirOcupante(@Body OcupanteCoche ocupante);

    @DELETE("api/coches/{id}")
    Call<Void> eliminarCoche(@Path("id") int id);

    @GET("api/coches/equipo/{idEquipo}")
    Call<List<CocheConOcupantesDTO>> obtenerCochesPorEquipo(@Path("idEquipo") int idEquipo);

    // 🔄 MULTAS
    @GET("api/multas/{idEquipo}")
    Call<List<Multa>> obtenerMultas(@Path("idEquipo") int idEquipo);

    @POST("api/multas")
    Call<Multa> crearMulta(@Body Multa multa);

    @PUT("api/multas/pagar/{idMulta}")
    Call<Void> marcarMultaPagada(@Path("idMulta") int idMulta);

    @DELETE("api/multas/{idMulta}")
    Call<Void> eliminarMulta(@Path("idMulta") int idMulta);

}
