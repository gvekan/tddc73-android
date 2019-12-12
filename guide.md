# Build your first app for Android with Kotlin

In this guide we will create an app for Android We will use Android Studio and the programming language Kotlin to develop this app. 

## Create the project

1. Install [Android Studio](https://developer.android.com/studio/).
2. When installed, open Android Studio and click **Start a new Android Studio project**.
3. Under the tab **Phone and Tablet** select **Empty Activity** and click **Next**.
4. Keep the **Name** and **Package name** as it is, you can change the **Save location** if you want. Select **Kotlin** as **Language**. Select **API 23** as **Minimum API level**. Make sure that the checkbox next to **Use androidx.\* artifacts** is checked. Click **Finish**.

## Project overview
Make sure that the **Project** window on the left side is open if not open it by pressing **Alt+1**.
Change the view in the **Project** window to **Android** with the dropdown at the top of the **Project** window. We will use the **Project** window to find the files we will work with.

Under **app->java->com.example.myapplication** we will find the Kotlin file **MainActivity** open it by double clicking on the file. In the file we will see a class **MainActivity** that extends **AppCompatActivity**. An instance of this class will be created when running the application. As we can see we have an overwritten method **onCreate** that is for now just used to set the layout of the activity.

We can find the layout under **app->res->layout->activity_main**. We can watch and change the file in both the designer view and text view. For now we have a **ConstraintLayout** as the base component. This component helps us layout all its children with constrains. We have also a **TextView** that is constrained to the center of the **ConstrainLayout**.

## Run your app
Follow this [guide](https://developer.android.com/training/basics/firstapp/running-app) to learn how to run your app on a real device or an emulator.

## Creating our first Fragment and the ViewModel
___
During the implementation of this app you will have to import classes to the files. If something is not imported it will be red and say "Unresolved reference". Import the thing by having the caret on the class where it is referenced. Press **Alt+Enter** and a menu will open. Click on import. If pressing **Alt+Enter** when prompted to import with **Alt+Enter** the menu will not show and the class will be imported.
___

In this app we will use so called **Fragments** on top of the **MainActivity** and navigate between these instead of navigating between multiple **Activities**. We will use a so called **ViewModel** to save data we will share between the **Fragments** the **ViewModel** will exist as long as the **Activity** it belongs to exists. For this app it is a little bit overkill to use a **ViewModel** but it is a good habit to use **ViewModels**.

### Prepare layout
In ``activity_main.xml`` remove the **TextView** and add a **FrameLayout** instead. 
Add the attributes:

**Designer** view:
* Set the **id** to *fragmentContainer*
* Under **Layout->Constraint Widget** add a top and a left constrain by clicking on the top and the left plus sign.

**Text** view:

Our file should look like this:
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <FrameLayout
        android:id="@+id/fragmentContainer"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
    
</androidx.constraintlayout.widget.ConstraintLayout>
```

### Add MainFragment

Right click on the folder **app->java->com.example.myapplication** and click on **New->Fragment->Fragment (with ViewModel)**. This will open a dialog. In the dialog change **Fragment Name** to *MainFragment*. Click on **Finish**. This should generate the Kotlin files ``MainFragment``, ``MainViewModel`` and a layout file ``main_fragment.xml``. 

Open ``MainFragment`` and in the method **onActivityCreated** replace
```kotlin
viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
```
with
```kotlin
viewModel = activity!!.run { ViewModelProviders.of(this).get(MainViewModel::class.java) }
```
to use the **ViewModel** attached to the **MainActivity**.

Open ``MainActivity`` and add the following to the **onCreate** method
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    
    ...
    if (savedInstanceState == null) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MainFragment.newInstance())
            .commitNow()    
    }
}
```
to attach the fragment to the **FrameLayout** we created in ``activity_main.xml``.

If we run the app we should see the text "Hello" in the upper left corner.

### Change MainFragment layout

We will do the following:

Open ``main_fragment.xml``.

Remove the **TextView**.

Replace the **FrameLayout** with a **ConstraintLayout**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainFragment"/>
```

Add an **EditText** *(Plain Text in Designer)* and constrain it to the top, left and right of the parent. Set the **id** to *editText* and remove the **text** and set **hint** to *Name*.

Add an **ListView** and constrain the top to the bottom of the **EditText**, the left and right to the parent. Set the **id** to *listView* and **layout_width** to *match_parent* and **layout_height** to *wrap_content*.

Now our file should look like the following:
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainFragment">

    <EditText
        android:id="@+id/editText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:ems="10"
        android:inputType="textPersonName"
        android:hint="Name"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ListView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/editText" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

Open ``MainFragment``.

Add the following property to the class:
```kotlin
private val items = listOf("Hello", "Bonjour", "Guten Tag", "Hej", "Ave")
```
Change **onCreateView** to:
```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.main_fragment, container, false)
    view.findViewById<ListView>(R.id.listView).apply {
        adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items)
    }
    return view
}
```
We use **findViewById** to find the **ListView** we created in the layout and when we fin it we set its adapter to a **ArrayAdapter** we create with our *items* property and a text layout provided in the android SDK. The **ArrayAdapter** will return a text layout for each string in the list to the **ListView**.

If we run the app we should see the **EditText** that says "Name" at the top and a list with each string under it.

## Put data in the ViewModel

Open ``MainViewModel`` and add the following property with get and set methods:
```kotlin
private val helloText = MutableLiveData<String>()

fun setHelloText(value: String) {
    helloText.value = value
}

fun getHelloText(): LiveData<String> {
    return helloText
}
```
We have the string contained in a **LiveData** object to observe the property and get notified when it changes.

Open ``MainFragment`` and let **MainFragment** implement the interface **AdapterView.OnItemClickListener** and thereby the method **onItemClick**.
```kotlin
class MainFragment : Fragment(), AdapterView.OnItemClickListener {
    
    ...

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}
```
In **onCreateView** set the **ListViews** onItemClickListener to the fragment.
```kotlin
...

view.findViewById<ListView>(R.id.listView).apply {
    adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items)
    onItemClickListener = this@MainFragment
}

...
```

The **onItemClick** method will be called when the user clicks on an item in the list. We will change the **ViewModel** when an item is clicked on. We do this by calling the **setHelloText** method in **onItemClick** and giving the item at the specific position as argument.
```kotlin
viewModel.setHelloText(items[position])
``` 
We also want to use the **EditText** component we added to the layout. Lets save it to a property in **onCreateView** and concatenate its text with the items text in **onItemClick**.
```kotlin
private lateinit var editText: EditText

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    ...
    editText = view.findViewById(R.id.editText)
    return view
}

...

override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    viewModel.setHelloText("${items[position]} ${editText.text}")
}
```

## Setting up navigation

Open ``MainActivity``. We will let the **MainActivity** handle the navigation between fragments. Let **MainActivity** implement the interface **FragmentManager.OnBackStackChangedListener** and thereby the method **onBackStackChanged**.
```kotlin
class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    ...

    override fun onBackStackChanged() {

    }
}
```
In **onCreate** add **MainActivity** as an **OnBackStackChangedListener** to the **FragmentManager**.
```kotlin
supportFragmentManager.addOnBackStackChangedListener(this)
```
Lets create a method **changeDisplayUp** in **MainActivity** that will decide if a back arrow should be displayed in the **ActionBar** on the left side of the app title.
```kotlin
private fun changeDisplayHomeUp() {
    val canGoBack = supportFragmentManager.backStackEntryCount > 0
    supportActionBar?.run { setDisplayHomeAsUpEnabled(canGoBack) }
}
```
In **changeDisplayUp** we check if our stack is filled with anything, if so we will enable the back arrow.
Call **changeDisplayUp** in both
**onCreate** and **onBackStackChanged**.

To handle user clicks on the back arrow we override the method **onSupportNavigateUp**. In this method we pop the stack to go back.
```kotlin
override fun onSupportNavigateUp(): Boolean {
    supportFragmentManager.popBackStack()
    return true
}
```

We want to change fragment when an item is clicked on in the **ListView** in the **MainFragment**. Right now, **MainActivity** is unaware of when this happens.

Open ``MainFragment`` and create the interface **OnItemClickListener** with the method **onItemClick**.
```kotlin
interface OnItemClickListener {
    fun onItemClick()
}
```
Create a property of the **OnItemClickListener** type and override the **onAttach** and **onDetach** methods. In **onAttach** we set our context as the **onItemClickListener** if the context implements the interface and in **onDetach** we set the listener to null.
```kotlin
...

private var onItemClickListener: OnItemClickListener? = null

...

override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is OnItemClickListener)
        onItemClickListener = context
}

override fun onDetach() {
    super.onDetach()
    onItemClickListener = null
}

...
```
Call the **onItemClick** method on the listener property.
```kotlin
override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    viewModel.setHelloText("${items[position]} ${editText.text}")
    onItemClickListener?.onItemClick()
}
```
Open ``MainActivity``. Let **MainActivity** implement the interface **MainFragment.OnItemClickListener** and thereby the **onItemClick** method.

Our **MainActivity** should look like the following:
```kotlin
class MainActivity : AppCompatActivity(), 
    FragmentManager.OnBackStackChangedListener, 
    MainFragment.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance())
                .commitNow()
        }
        supportFragmentManager.addOnBackStackChangedListener(this)
        changeDisplayHomeUp()
    }

    override fun onBackStackChanged() {
        changeDisplayHomeUp()
    }

    private fun changeDisplayHomeUp() {
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.run { setDisplayHomeAsUpEnabled(canGoBack) }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return true
    }

    override fun onItemClick() {
        
    }
}
```
## Finishing the app with HelloFragment

Right click on the folder **app->java->com.example.myapplication** and click on **New->Fragment->Fragment (with ViewModel)**. This will open a dialog. In the dialog change **Fragment Name** to *HelloFragment*. Click on **Finish**. This should generate the Kotlin files ``HelloFragment``, ``HelloViewModel`` and a layout file ``hello_fragment.xml``. 

Delete ``HelloViewModel``.

Open ``hello_fragment``. In the **TextView**:
* Set **id** to *textView*.
* Set the **textSize** to *30sp*.
* Set the **layout_margin** to *16dp*.

The file should look like the following:
```xml
<FrameLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".HelloFragment">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="Hello"
        android:textSize="30sp"
        android:layout_margin="16dp"/>

</FrameLayout>
```
Open ``HelloFragment`` and in the method **onActivityCreated** replace
```kotlin
viewModel = ViewModelProviders.of(this).get(HelloViewModel::class.java)
```
with
```kotlin
viewModel = activity!!.run { ViewModelProviders.of(this).get(MainViewModel::class.java) }
```
and change the property **viewModel** to the **MainViewModel** type.
```kotlin
private lateinit var viewModel: MainViewModel
```
Create a property to save the **TextView** in the layout to in **onCreateView** and save the **TextView** to it in the **onCreateView** method.
```kotlin
private lateinit var textView: TextView

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    return inflater.inflate(R.layout.hello_fragment, container, false).also { 
        textView = it.findViewById(R.id.textView)
    }
}
```
We want to set the text of the **TextView** to the **helloText** in the **MainViewModel**. In **onActivityCreated** call **getHelloText** on the **ViewModel** and attach  an observer. In the observer set the **TextView** text to the **helloText**.
```kotlin
viewModel.getHelloText().observe(this, Observer {helloString ->
    textView.text = helloString
})
``` 
### Navigate to HelloFragment
Open ``MainActivity``. In **onItemClick** commit a transaction to the **FragmentManager** to replace **MainFragment** with **HelloFragment**.
```kotlin
override fun onItemClick() {
    supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragmentContainer, HelloFragment.newInstance())
        addToBackStack(null)
    }.commit()
}
``` 

## Run the app
We are now finished with the app. When we start the app we can write a name and then click on a hello phrase that takes us to a page where we see the hello phrase followed by the name. We can get back to the first page with the help of a back arrow. 