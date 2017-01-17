package alice.util;

import java.lang.reflect.Method;


public class InspectionUtils
{
	
	
	public static Method searchForMethod(Class<?> type, String methodName, Class<?>[] parms) {
	    Method[] methods = type.getMethods();
	    for(int i = 0; i < methods.length; i++) {
	        // Has to be named the same of course.
	        if( !methods[i].getName().equals(methodName))
	            continue;

	        Class<?>[] types = methods[i].getParameterTypes();

	        // Does it have the same number of arguments that we're looking for.
	        if( types.length != parms.length )
	            continue;

	        // Check for type compatibility
	        if(alice.util.InspectionUtils.areTypesCompatible(types, parms))
	            return methods[i];
	        }
	    return null;
	}
	
   
    public static boolean areTypesCompatible(Class<?>[] targets, Class<?>[] sources)
    {
        if(targets.length != sources.length)
            return( false );

        for(int i = 0; i < targets.length; i++)
        {
            if(sources[i] == null)
                continue;

            if(targets[i].isInterface())
            {
            	Class<?>[] interfaces = sources[i].getInterfaces();
            	for (Class<?> in : interfaces) {
					if(targets[i].equals(in))
						return true;
				}
            }
            	
            if(!translateFromPrimitive(targets[i]).isAssignableFrom(translateFromPrimitive(sources[i])))
                return false;
        }
        return true;
    }

    
    public static Class<?> translateFromPrimitive(Class<?> primitive)
    {
        if(!primitive.isPrimitive())
            return(primitive);

        if(Boolean.TYPE.equals(primitive))
            return( Boolean.class );
        if(Character.TYPE.equals(primitive))
            return(Character.class);
        if(Byte.TYPE.equals(primitive))
            return( Byte.class);
        if(Short.TYPE.equals(primitive))
            return( Short.class);
        if(Integer.TYPE.equals(primitive))
            return(Integer.class);
        if(Long.TYPE.equals(primitive))
            return(Long.class);
        if(Float.TYPE.equals(primitive))
            return(Float.class);
        if(Double.TYPE.equals(primitive))
            return(Double.class);

        throw new RuntimeException("Error translating type:" + primitive);
    }
}
