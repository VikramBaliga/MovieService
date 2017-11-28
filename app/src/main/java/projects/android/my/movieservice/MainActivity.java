package projects.android.my.movieservice;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String url = "http://api.themoviedb.org/3/tv/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(CheckInternetConnectivity())
        {
            Toast.makeText(this,"Internet Connected",Toast.LENGTH_LONG).show();
            MovieService weatherTask = new MovieService(this,MainActivity.this);
            weatherTask.execute(url);
        }
        else
        {
            Toast.makeText(this,"Internet Not Connected",Toast.LENGTH_LONG).show();
        }
    }

    private boolean CheckInternetConnectivity()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().isConnected();
    }
}
class MovieService extends AsyncTask
{
    StringBuilder builder = new StringBuilder();
    Activity activity;
    Context context;
    public MovieService(Activity activity, Context context)
    {
        this.activity=activity;
        this.context=context;
    }

    @Override
    protected Object doInBackground(Object[] params)
    {
        Log.i("Inside Service",params[0].toString());

        try
        {
            URL url = new URL(params[0].toString());
            URLConnection con =  url.openConnection();
            con.connect();
            InputStream stream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String temp="";
            while ((temp = reader.readLine())!=null)
            {
                builder.append(temp);
            }
            Log.i("Service Data",builder.toString());


           // MovieAdapter movieAdapter = new MovieAdapter(movieName,vote_count,id,rating);
        }
        catch (Exception ex)
        {
            Log.e("ConnectionError","Error in connecting");
            Log.e("Error",ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        try
        {
            JSONObject object = new JSONObject(builder.toString());
            JSONArray movieList = object.getJSONArray("results");

            String[] movieName = new String[]{"m1","m2","m3","m4","m5"};
            String[] vote_count = new String[]{"1","2","3","4","5"};
            String[] id = new String[]{"100","200","300","400","500"};
            String[] rating = new String[]{"3","3","3","3","3"};

           for(int i=0;i<movieList.length();i++)
            {
                movieName[i]=movieList.getJSONObject(i).getString("original_name");
                vote_count[i]=movieList.getJSONObject(i).getString("vote_count");
                id[i]=movieList.getJSONObject(i).getString("id");
                rating[i]=movieList.getJSONObject(i).getString("vote_average");
            }

            ListView moviesList = (ListView) activity.findViewById(R.id.movieList);
            MovieAdapter movieAdapter = new MovieAdapter(movieName,vote_count,id,rating,context);
            moviesList.setAdapter(movieAdapter);
        }
        catch (Exception ex)
        {

        }
    }
}


class MovieAdapter extends BaseAdapter
{
    String[] movieName;
    String[] vote_count;
    String[] id;
    String[] rating;
    Context context;
    public MovieAdapter(String[] movieName,String[] vote_count,String[] id,String[] rating,Context context)
    {
        this.movieName=movieName;
        this.vote_count=vote_count;
        this.id=id;
        this.rating=rating;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_list,null);

        TextView textView = (TextView) view.findViewById(R.id.txtMovieName);
        textView.setText(movieName[position]);
        return  view;
    }
}


