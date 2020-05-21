package org.kx.util.reflect;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class CompilingObjectMaker {

    public static void main(String[] args) throws ClassNotFoundException {




        String progClass ="com.ailk.dynamic.Demo";

// 创建CompilingClassLoader
        Class c = Class.forName(progClass, true, new CompilingClassLoader());
     //   DemoInterface i=(DemoInterface)c.newInstance();

//cl1和cl2是两个不同的ClassLoader
      //  ClassLoader cl1=c.getClassLoader();
      //  ClassLoader cl2=Demo.class.getClassLoader();
      //  ClassLoader cl3=DemoInterface.class.getClassLoader();


       // int ii=0;
       // List<DemoInterface> objList=new ArrayList();
       /* while(true){
            ii++;
            CompilingClassLoader ccl = new CompilingClassLoader();
// 通过CCL加载主函数类。
            Class clas = ccl.loadClass( progClass,true);
// 利用反射调用它的函数和传递参数。
// 产生一个代表主函数的参数类型的类对象。
            Class mainArgType[] = { String.class };
// 在类中找函数。
            Method method = clas.getMethod( "print", mainArgType );
            Object[] argsArray={"Demo"};
//调用方法。
            method.invoke( clas.newInstance(), argsArray );

            ccl=null;//这里讲主动释放cc1
            DemoInterface instance=(DemoInterface)clas.newInstance();
            objList.add(instance);
            instance.print("demo");
            if (ii>20)
            {
                ii=0;
                objList.clear();
            }
            Thread.sleep(500);
//强制gc,只有objList清空后 CompilingClassLoader的实例才会释放。
//因为只有由CompilingClassLoader载入的class的实例全部释放后，CompilingClassLoader才能被释放
            System.gc();

        }*/
   // }

    }
}
