package software.com.findmenear.adapters;

import software.com.findmenear.R;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ToolbarAdapter extends RecyclerView.Adapter<ToolbarAdapter.ViewHolder>{

  private static final int TYPE_HEADER = 0;  // Dichiara la variabile per capire quale view è a lavoro

  private static final int TYPE_ITEM = 1;

  private String mNavTitles[]; // String Array per memorizzare il titolo passato dall DrawerActivity.java
  private int mIcons[];       // Int Array per memorizzare l'icona dall DrawerActivity.java

  private String name;        //String Resource per l'header View Name
  private int profile;        //int Resource per l'header view profile picture
  private String email;       //String Resource per l'header view email
  Context context;

  public ToolbarAdapter(String Titles[], int Icons[], String Name, String Email, int Profile, Context passedContext) { // MyAdapter Constructor  con titoli e le icone
    // titles, icons, name, email, profile pic sono passati dal main activity come segue:
    mNavTitles = Titles;
    mIcons = Icons;
    name = Name;
    email = Email;
    profile = Profile;                     //qui assegniamo quei valori passati dichiarati qui
    this.context = passedContext;
    //in adapter
  }


  //qui prima di fare l'override del onCreateViewHolder che è chiamato quando il ViewHolder è
  //creato, in questo metodo lanciamo item_row.xml layout se il viewType è Type_ITEM opure lanciamo header.xml
  // se il viewType è TYPE_HEADER
  // e lo passiamo  al view holder

  @Override
  public ToolbarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == TYPE_ITEM) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //lanciamo il layout
      ViewHolder vhItem = new ViewHolder(v, viewType, context); //Creazione ViewHolder e passaggio del tipo di oggetto alal view
      return vhItem; // Ritorna l'oggetto creato
      //Lancia il nostro layout e viene passato al view holder
    } else if (viewType == TYPE_HEADER) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //lanciamo il layout layout
      ViewHolder vhHeader = new ViewHolder(v, viewType, context); //Creazione ViewHolder e passaggio del tipo di oggetto
      return vhHeader; //ritorna l'oggetto creato
    }
    return null;
  }

  //successivamente facciamo l'override del metodo che sarà chiamato quando item nella riga dovrà essere visualizzato.
  // Ci dice voce in quale posizione si sta costruendo da visualizzare e l'id titolare del portaoggetti ci dicono
  // quale tipo di view deve venire creata
  @Override
  public void onBindViewHolder(ToolbarAdapter.ViewHolder holder, int position) {
    if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
      // position by 1 and pass it to the holder while setting the text and image
      holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
      holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
    } else {
      //holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
      holder.Name.setText(name);
      holder.email.setText(email);
    }
  }

  // This method returns the number of items present in the list
  @Override
  public int getItemCount() {
    return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
  }


  // Witht the following method we check what type of view is being passed
  @Override
  public int getItemViewType(int position) {
    if (isPositionHeader(position))
      return TYPE_HEADER;

    return TYPE_ITEM;
  }

  private boolean isPositionHeader(int position) {
    return position == 0;
  }


  //-- Toolbar View Holder ------------------------------------------------------------------------

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    int Holderid;

    TextView textView;
    ImageView imageView;
    //ImageView profile;
    TextView Name;
    TextView email;
    Context contxt;


    public ViewHolder(View itemView, int ViewType, Context c) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
      super(itemView);
      contxt = c;
      itemView.setClickable(true);
      itemView.setOnClickListener(this);
      // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

      if (ViewType == TYPE_ITEM) {
        textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
        imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
        Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
      } else {
        Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
        email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
        //profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
        Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
      }
    }

    @Override
    public void onClick(View v) {
      Toast.makeText(contxt, "The Item Clicked is: " + getPosition(), Toast.LENGTH_SHORT).show();
    }
  }
}
