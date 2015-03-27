package de.tpronold.gdxfacebook.core.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.tpronold.gdxfacebook.core.FacebookAPI;
import de.tpronold.gdxfacebook.core.FacebookConfig;
import de.tpronold.gdxfacebook.core.FacebookSystem;
import de.tpronold.gdxfacebook.core.tests.stubs.ActivityStub;
import de.tpronold.gdxfacebook.core.tests.stubs.FacebookAPIStub;
import de.tpronold.gdxfacebook.core.tests.stubs.FragmentStub;
import de.tpronold.gdxfacebook.core.tests.stubs.GdxStub;
import de.tpronold.gdxfacebook.core.tests.stubs.SupportFragmentStub;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ClassReflection.class, Field.class, Constructor.class, Method.class })
public class FacebookSystemUnitTests {

	private FacebookSystem fixture;
	private FacebookConfig config;

	private ClassReflection classReflectionMock;
	private Field fieldMock;
	private Constructor constructorMock;
	private Method methodMock;

	private FacebookAPI facebookAPIStub;

	private GdxStub gdxStub;
	// private GdxLifecycleListenerStub gdxLifecycleListenerStub;
	private ActivityStub activityStub;
	private SupportFragmentStub supportFragmentStub;
	private FragmentStub fragmentStub;

	@Before
	@SuppressWarnings("static-access")
	public void setup() {
		config = new FacebookConfig();

		PowerMockito.mockStatic(ClassReflection.class);
		classReflectionMock = Mockito.mock(ClassReflection.class);

		PowerMockito.mockStatic(Field.class);
		fieldMock = Mockito.mock(Field.class);

		PowerMockito.mockStatic(Constructor.class);
		constructorMock = Mockito.mock(Constructor.class);

		PowerMockito.mockStatic(Method.class);
		methodMock = Mockito.mock(Method.class);

		Gdx.app = Mockito.mock(HeadlessApplication.class);

		gdxStub = new GdxStub();
		// gdxLifecycleListenerStub = new GdxLifecycleListenerStub();
		activityStub = new ActivityStub();
		supportFragmentStub = new SupportFragmentStub();
		fragmentStub = new FragmentStub();

		try {
			Mockito.when(classReflectionMock.forName("com.badlogic.gdx.Gdx")).thenReturn(gdxStub.getClass());
			// Mockito.when(classReflectionMock.forName("com.badlogic.gdx.LifecycleListener")).thenReturn(fakeGdxLifecycleListenerClazz);

			Mockito.when(classReflectionMock.getField(gdxStub.getClass(), "app")).thenReturn(fieldMock);
			Mockito.when(fieldMock.get(null)).thenReturn(Gdx.app);

			// Mockito.when(classReflectionMock.getMethod(Gdx.app.getClass(),
			// "getActivity", fakeGdxLifecycleListenerClazz)).thenReturn(null);

		} catch (ReflectionException e) {
		}

		facebookAPIStub = new FacebookAPIStub();

	}

	@Test
	@SuppressWarnings("static-access")
	public void DesktopIsLoaded() {
		config.ENABLE_DESKTOP = true;
		Mockito.when(Gdx.app.getType()).thenReturn(ApplicationType.Desktop);
		try {

			Mockito.when(classReflectionMock.forName("de.tpronold.gdxfacebook.desktop.DesktopFacebookAPI")).thenReturn(facebookAPIStub.getClass());
			Mockito.when(classReflectionMock.getConstructor(facebookAPIStub.getClass(), FacebookConfig.class)).thenReturn(constructorMock);
			Mockito.when(constructorMock.newInstance(config)).thenReturn(facebookAPIStub);
		} catch (ReflectionException e) {
		}

		fixture = new FacebookSystem(config);
		assertEquals(facebookAPIStub, fixture.getFacebookAPI());
	}

	@Test
	@SuppressWarnings("static-access")
	public void HTMLIsLoaded() {
		config.ENABLE_HTML = true;
		Mockito.when(Gdx.app.getType()).thenReturn(ApplicationType.WebGL);
		try {
			Mockito.when(classReflectionMock.forName("de.tpronold.gdxfacebook.html.HTMLFacebookAPI")).thenReturn(facebookAPIStub.getClass());
			Mockito.when(classReflectionMock.getConstructor(facebookAPIStub.getClass(), FacebookConfig.class)).thenReturn(constructorMock);
			Mockito.when(constructorMock.newInstance(config)).thenReturn(facebookAPIStub);
		} catch (ReflectionException e) {
		}

		fixture = new FacebookSystem(config);
		assertEquals(facebookAPIStub, fixture.getFacebookAPI());
	}

	@Test
	@SuppressWarnings("static-access")
	public void iOSIsLoaded() {
		config.ENABLE_IOS = true;
		try {
			Mockito.when(Gdx.app.getType()).thenReturn(ApplicationType.iOS);
			Mockito.when(classReflectionMock.forName("de.tpronold.gdxfacebook.ios.IOSFacebookAPI")).thenReturn(facebookAPIStub.getClass());
			Mockito.when(classReflectionMock.getConstructor(facebookAPIStub.getClass(), FacebookConfig.class)).thenReturn(constructorMock);
			Mockito.when(constructorMock.newInstance(config)).thenReturn(facebookAPIStub);
		} catch (ReflectionException e) {
		}

		fixture = new FacebookSystem(config);
		assertEquals(facebookAPIStub, fixture.getFacebookAPI());
	}

	@SuppressWarnings("static-access")
	private void androidSetup() {
		config.ENABLE_ANDROID = true;

		try {
			Mockito.when(classReflectionMock.forName("android.app.Activity")).thenReturn(activityStub.getClass());
			Mockito.when(classReflectionMock.forName("de.tpronold.gdxfacebook.android.AndroidFacebookAPI")).thenReturn(facebookAPIStub.getClass());
			Mockito.when(Gdx.app.getType()).thenReturn(ApplicationType.Android);
			Mockito.when(classReflectionMock.getConstructor(facebookAPIStub.getClass(), activityStub.getClass(), FacebookConfig.class)).thenReturn(constructorMock);

		} catch (ReflectionException e) {
		}
	}

	@Test
	@SuppressWarnings("static-access")
	public void androidIsLoadedWithActivityMode() {
		androidSetup();

		try {
			Mockito.when(constructorMock.newInstance(Gdx.app, config)).thenReturn(facebookAPIStub);
		} catch (ReflectionException e) {
			e.printStackTrace();
		}

		Mockito.when(classReflectionMock.isAssignableFrom(activityStub.getClass(), Gdx.app.getClass())).thenReturn(true);

		fixture = new FacebookSystem(config);
		assertEquals(facebookAPIStub, fixture.getFacebookAPI());
	}

	@Test
	@SuppressWarnings("static-access")
	public void androidIsLoadedWithV4Fragment() {
		androidSetup();

		Mockito.when(classReflectionMock.isAssignableFrom(activityStub.getClass(), Gdx.app.getClass())).thenReturn(false);
		Mockito.when(classReflectionMock.isAssignableFrom(supportFragmentStub.getClass(), Gdx.app.getClass())).thenReturn(true);

		try {
			Mockito.when(constructorMock.newInstance(activityStub, config)).thenReturn(facebookAPIStub);
			Mockito.when(classReflectionMock.forName("android.support.v4.app.Fragment")).thenReturn(supportFragmentStub.getClass());
			Mockito.when(classReflectionMock.getMethod(supportFragmentStub.getClass(), "getActivity")).thenReturn(methodMock);
			Mockito.when(methodMock.invoke(Gdx.app)).thenReturn(activityStub);

		} catch (ReflectionException e) {
		}

		fixture = new FacebookSystem(config);

		assertEquals(facebookAPIStub, fixture.getFacebookAPI());
	}

	@Test
	@SuppressWarnings("static-access")
	public void androidIsLoadedWithFragment() {
		androidSetup();

		Mockito.when(classReflectionMock.isAssignableFrom(activityStub.getClass(), Gdx.app.getClass())).thenReturn(false);
		Mockito.when(classReflectionMock.isAssignableFrom(supportFragmentStub.getClass(), Gdx.app.getClass())).thenReturn(false);
		Mockito.when(classReflectionMock.isAssignableFrom(fragmentStub.getClass(), Gdx.app.getClass())).thenReturn(true);

		try {
			Mockito.when(constructorMock.newInstance(activityStub, config)).thenReturn(facebookAPIStub);
			Mockito.when(classReflectionMock.forName("android.app.Fragment")).thenReturn(fragmentStub.getClass());
			Mockito.when(classReflectionMock.getMethod(fragmentStub.getClass(), "getActivity")).thenReturn(methodMock);
			Mockito.when(methodMock.invoke(Gdx.app)).thenReturn(activityStub);

		} catch (ReflectionException e) {
		}

		fixture = new FacebookSystem(config);
		assertEquals(facebookAPIStub, fixture.getFacebookAPI());
	}
}