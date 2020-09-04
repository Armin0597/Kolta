package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFinalDocAct extends AppCompatActivity {

    EditText bukti_krs, spp_tetap, spp_variable, srt_pendadaran, byr_pendadaran,kartu_bimb;
    ImageView ic_bukti_krs, ic_spp_tetap, ic_spp_variable, ic_srt_pendadaran, ic_byr_pendadaran, ic_kartu_bimb;
    Button btn_continue;
    LinearLayout btn_back;

    DatabaseReference reference;
    StorageReference storage;

    Uri doc_location;
    Integer doc_max = 5;

    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    private static final int SELECT_FILE3 = 3;
    private static final int SELECT_FILE4 = 4;
    private static final int SELECT_FILE5 = 5;
    private static final int SELECT_FILE6 = 6;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_final_doc);

        getUsernameLocal();

        bukti_krs = findViewById(R.id.bukti_krs);
        spp_tetap = findViewById(R.id.spp_tetap);
        spp_variable = findViewById(R.id.spp_variable);
        srt_pendadaran = findViewById(R.id.srt_pendadaran);
        byr_pendadaran = findViewById(R.id.byr_pendadaran);
        kartu_bimb = findViewById(R.id.kartu_bimb);
        ic_bukti_krs = findViewById(R.id.ic_bukti_krs);
        ic_spp_tetap = findViewById(R.id.ic_spp_tetap);
        ic_spp_variable = findViewById(R.id.ic_spp_variabel);
        ic_srt_pendadaran = findViewById(R.id.ic_srt_pendadaran);
        ic_byr_pendadaran = findViewById(R.id.ic_byr_pendadaran);
        ic_kartu_bimb = findViewById(R.id.ic_kartu_bimb);
        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);

//        reference = FirebaseDatabase.getInstance().getReference().child("DrafFinal").child(username_key_new).child("berkas");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                bukti_krs.setText(dataSnapshot.child("bukti_krs").getValue().toString());
//                spp_tetap.setText(dataSnapshot.child("spp_tetap").getValue().toString());
//                spp_variable.setText(dataSnapshot.child("spp_variabel").getValue().toString());
//                srt_pendadaran.setText(dataSnapshot.child("srt_pendadaran").getValue().toString());
//                byr_pendadaran.setText(dataSnapshot.child("bayar_pendadaran").getValue().toString());
//                kartu_bimb.setText(dataSnapshot.child("kartu_bimbingan").getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ic_bukti_krs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE1);
            }
        });
        ic_spp_tetap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE2);
            }
        });
        ic_spp_variable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE3);
            }
        });
        ic_spp_variable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE3);
            }
        });
        ic_srt_pendadaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE4);
            }
        });
        ic_byr_pendadaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE5);
            }
        });
        ic_kartu_bimb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE6);
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");

                Intent gotoaddfinaldoctwo = new Intent(AddFinalDocAct.this, AddFinalDocTwoAct.class);
                startActivity(gotoaddfinaldoctwo);

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotofinaldoc = new Intent(AddFinalDocAct.this, FinalDocAct.class);
                startActivity(gotofinaldoc);
            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //menemukan dokumen
    public void findDoc(int req_code){
        Intent doc = new Intent();
        doc.setType("*/*");
        doc.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(doc, "select file to upload"), req_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        getUsernameLocal();
        final ProgressDialog pd = new ProgressDialog(this);
        final String nama_tugas_baru = getIntent().getStringExtra("nama_tugas");

        if (resultCode == RESULT_OK) {
            int counter = 1;
            doc_location = data.getData();
            if(requestCode == SELECT_FILE1){
                final String buktiKrs;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                buktiKrs = "brks_"+"buktiKrs"+"_"+time+"."+getFileExtension(doc_location);
                bukti_krs.setText(buktiKrs);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(buktiKrs);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("bukti_krs").setValue(buktiKrs);
                                            reference.getRef().child("url_bukti_krs").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE2){
                final String sppTetap;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                sppTetap = "brks_"+"sppTetap"+"_"+time+"."+getFileExtension(doc_location);
                spp_tetap.setText(sppTetap);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(sppTetap);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("spp_tetap").setValue(sppTetap);
                                            reference.getRef().child("url_spp_tetap").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE3){
                final String sppVariabel;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                sppVariabel = "brks_"+"sppVariabel"+"_"+time+"."+getFileExtension(doc_location);
                spp_variable.setText(sppVariabel);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(sppVariabel);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("spp_variabel").setValue(sppVariabel);
                                            reference.getRef().child("url_spp_variabel").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE4){
                final String srtPendadaran;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                srtPendadaran = "brks_"+"srtPendadaran"+"_"+time+"."+getFileExtension(doc_location);
                srt_pendadaran.setText(srtPendadaran);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(srtPendadaran);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("srt_pendadaran").setValue(srtPendadaran);
                                            reference.getRef().child("url_srt_pendadaran").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE5){
                final String byrPendadaran;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                byrPendadaran = "brks_"+"byrPendadaran"+"_"+time+"."+getFileExtension(doc_location);
                byr_pendadaran.setText(byrPendadaran);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(byrPendadaran);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("bayar_pendadaran").setValue(byrPendadaran);
                                            reference.getRef().child("url_bayar_pendadaran").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }
            }else if(requestCode == SELECT_FILE6){
                final String krtBimb;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                krtBimb = "brks_"+"kartuBimb"+"_"+time+"."+getFileExtension(doc_location);
                kartu_bimb.setText(krtBimb);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(krtBimb);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("kartu_bimbingan").setValue(krtBimb);
                                            reference.getRef().child("url_kartu_bimbingan").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }
            }

        }
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}