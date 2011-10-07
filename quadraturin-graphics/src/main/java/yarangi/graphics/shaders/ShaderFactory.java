package yarangi.graphics.shaders;

import java.util.Map;

import javax.media.opengl.GL;

import yarangi.graphics.quadraturin.plugin.IGraphicsPlugin;
import yarangi.graphics.quadraturin.resources.ResourceFactory;

public class ShaderFactory extends ResourceFactory <IShader> implements IGraphicsPlugin
{
	
	// GL_AMDX_debug_output GL_AMDX_vertex_shader_tessellator GL_AMD_conservative_depth GL_AMD_debug_output GL_AMD_draw_buffers_blend 
	// GL_AMD_name_gen_delete GL_AMD_performance_monitor GL_AMD_sample_positions GL_AMD_shader_stencil_export GL_AMD_texture_cube_map_array 
	// GL_AMD_texture_texture4 GL_AMD_vertex_shader_tessellator GL_ARB_blend_func_extended GL_ARB_color_buffer_float GL_ARB_copy_buffer 
	// GL_ARB_depth_buffer_float GL_ARB_depth_clamp GL_ARB_depth_texture GL_ARB_draw_buffers GL_ARB_draw_buffers_blend 
	// GL_ARB_draw_elements_base_vertex GL_ARB_draw_instanced GL_ARB_explicit_attrib_location GL_ARB_fragment_coord_conventions 
	// GL_ARB_fragment_program GL_ARB_fragment_program_shadow GL_ARB_fragment_shader GL_ARB_framebuffer_object GL_ARB_framebuffer_sRGB 
	// GL_ARB_geometry_shader4 GL_ARB_half_float_pixel GL_ARB_half_float_vertex GL_ARB_imaging GL_ARB_instanced_arrays GL_ARB_map_buffer_range 
	// GL_ARB_multisample GL_ARB_multitexture GL_ARB_occlusion_query GL_ARB_occlusion_query2 GL_ARB_pixel_buffer_object GL_ARB_point_parameters 
	// GL_ARB_point_sprite GL_ARB_provoking_vertex GL_ARB_sample_shading GL_ARB_sampler_objects GL_ARB_seamless_cube_map 
	// GL_ARB_shader_bit_encoding GL_ARB_shader_objects GL_ARB_shader_texture_lod GL_ARB_shading_language_100 GL_ARB_shadow 
	// GL_ARB_shadow_ambient GL_ARB_sync GL_ARB_texture_border_clamp GL_ARB_texture_buffer_object GL_ARB_texture_compression 
	// GL_ARB_texture_compression_rgtc GL_ARB_texture_cube_map GL_ARB_texture_cube_map_array GL_ARB_texture_env_add GL_ARB_texture_env_combine 
	// GL_ARB_texture_env_crossbar GL_ARB_texture_env_dot3 GL_ARB_texture_float GL_ARB_texture_gather GL_ARB_texture_mirrored_repeat 
	// GL_ARB_texture_multisample GL_ARB_texture_non_power_of_two GL_ARB_texture_rectangle GL_ARB_texture_rg GL_ARB_texture_rgb10_a2ui 
	// GL_ARB_texture_snorm GL_ARB_timer_query GL_ARB_transform_feedback2 GL_ARB_transform_feedback3 GL_ARB_transpose_matrix 
	// GL_ARB_uniform_buffer_object GL_ARB_vertex_array_bgra GL_ARB_vertex_array_object GL_ARB_vertex_buffer_object GL_ARB_vertex_program 
	// GL_ARB_vertex_shader GL_ARB_vertex_type_2_10_10_10_rev GL_ARB_window_pos GL_ATI_draw_buffers GL_ATI_envmap_bumpmap
	// GL_ATI_fragment_shader GL_ATI_meminfo GL_ATI_separate_stencil GL_ATI_texture_compression_3dc GL_ATI_texture_env_combine3 
	// GL_ATI_texture_float GL_ATI_texture_mirror_once GL_EXT_abgr GL_EXT_bgra GL_EXT_bindable_uniform GL_EXT_blend_color 
	// GL_EXT_blend_equation_separate GL_EXT_blend_func_separate GL_EXT_blend_minmax GL_EXT_blend_subtract GL_EXT_compiled_vertex_array 
	// GL_EXT_copy_buffer GL_EXT_copy_texture GL_EXT_direct_state_access GL_EXT_draw_buffers2 GL_EXT_draw_instanced GL_EXT_draw_range_elements 
	// GL_EXT_fog_coord GL_EXT_framebuffer_blit GL_EXT_framebuffer_multisample GL_EXT_framebuffer_object GL_EXT_framebuffer_sRGB 
	// GL_EXT_geometry_shader4 GL_EXT_gpu_program_parameters GL_EXT_gpu_shader4 GL_EXT_histogram GL_EXT_multi_draw_arrays 
	// GL_EXT_packed_depth_stencil GL_EXT_packed_float GL_EXT_packed_pixels GL_EXT_pixel_buffer_object GL_EXT_point_parameters 
	// GL_EXT_provoking_vertex GL_EXT_rescale_normal GL_EXT_secondary_color GL_EXT_separate_specular_color GL_EXT_shadow_funcs 
	// GL_EXT_stencil_wrap GL_EXT_subtexture GL_EXT_texgen_reflection GL_EXT_texture3D GL_EXT_texture_array GL_EXT_texture_buffer_object 
	// GL_EXT_texture_buffer_object_rgb32 GL_EXT_texture_compression_latc GL_EXT_texture_compression_rgtc GL_EXT_texture_compression_s3tc 
	// GL_EXT_texture_cube_map GL_EXT_texture_edge_clamp GL_EXT_texture_env_add GL_EXT_texture_env_combine GL_EXT_texture_env_dot3 
	// GL_EXT_texture_filter_anisotropic GL_EXT_texture_integer GL_EXT_texture_lod GL_EXT_texture_lod_bias GL_EXT_texture_mirror_clamp 
	// GL_EXT_texture_object GL_EXT_texture_rectangle GL_EXT_texture_sRGB GL_EXT_texture_shared_exponent GL_EXT_texture_snorm 
	// GL_EXT_texture_swizzle GL_EXT_timer_query GL_EXT_transform_feedback GL_EXT_vertex_array GL_EXT_vertex_array_bgra 
	// GL_IBM_texture_mirrored_repeat GL_KTX_buffer_region GL_NV_blend_square GL_NV_conditional_render GL_NV_copy_depth_to_color 
	// GL_NV_explicit_multisample GL_NV_float_buffer GL_NV_half_float GL_NV_primitive_restart GL_NV_texgen_reflection GL_SGIS_generate_mipmap 
	// GL_SGIS_texture_edge_clamp GL_SGIS_texture_lod GL_SUN_multi_draw_arrays GL_WIN_swap_hint WGL_EXT_swap_control
	
	
	
	public ShaderFactory(String factoryName, Map <String, String> properties)
	{
		super(factoryName);
		
		for(String shaderName : properties.keySet())
		{
			registerResource(shaderName, new FileShader(properties.get(shaderName)));
		}
	}
	
	public void init(GL gl)
	{
		for(IShader shaderResource : getHandles().values())
		{
			shaderResource.init(gl);
		}
	}
	
	
	public IShader getShader(String resourceId)
	{
		return getResource(resourceId);
	}

	public String toString() 
	{
		return "shader-factory";
	}

	@Override
	public String[] getRequiredExtensions()
	{
		return new String [] { "GL_ARB_vertex_shader", "GL_ARB_fragment_shader"};
	}
}
