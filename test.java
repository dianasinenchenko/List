    private List<Kip> getChangeKipList(final List<Kip> kipList) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    if (kipList == null) {

//                        presenter.requestDataFromApi();
//                        presenter.request();
//                        showProgress();
                    }


                    for (int i = 0; i < kipList.size(); i++) {

                        for (int ii = 0; ii < objectModels.size(); ii++) {


                            String idObject = kipList.get(i).getObjectId();
                            String idFromObjModel = objectModels.get(ii).getId();

                            GPSTracker gpsTracker = new GPSTracker(getActivity());

                            if (gpsTracker.canGetLocation()) {


                                Location userLocation = new Location("");
                                userLocation.setLatitude(gpsTracker.getLatitude());
                                userLocation.setLongitude(gpsTracker.getLongitude());

                                Location markerLocation = new Location("");
                                markerLocation.setLatitude(Double.parseDouble(kipList.get(i).getLatitude()));
                                markerLocation.setLongitude(Double.parseDouble(kipList.get(i).getLongitude()));


                                if (idObject.equals(idFromObjModel)) {

                                    String straightDistanceInKm = null;
                                    String distanceInKmAfterSplit = null;


                                    DirectionsResult results = getDirectionsDetails(gpsTracker.getLatitude() + "," + gpsTracker.getLongitude(),
                                            Double.parseDouble(kipList.get(i).getLatitude()) + "," + Double.parseDouble(kipList.get(i).getLongitude()),
                                            TravelMode.DRIVING);

                                    if (results != null) {

                                        String[] distanceSplit = getEndLocationTitle(results).split(" ");
                                        distanceInKmAfterSplit = distanceSplit[0].replace(",", "");


                                        Kip kip = new Kip(
                                                kipList.get(i).getId(),
                                                kipList.get(i).getName(),
                                                kipList.get(i).getSubjectCode(),
                                                objectModels.get(ii).getObjectName(),
                                                kipList.get(i).getLongitude(),
                                                kipList.get(i).getLatitude(),
                                                kipList.get(i).getKm(),
                                                distanceInKmAfterSplit
                                        );
                                        kipList.set(i, kip);




                                    } else {
                                        straightDistanceInKm = String.valueOf(userLocation.distanceTo(markerLocation) / 1000);


                                        Kip kip = new Kip(
                                                kipList.get(i).getId(),
                                                kipList.get(i).getName(),
                                                kipList.get(i).getSubjectCode(),
                                                objectModels.get(ii).getObjectName(),
                                                kipList.get(i).getLongitude(),
                                                kipList.get(i).getLatitude(),
                                                kipList.get(i).getKm(),
                                                straightDistanceInKm
                                        );

                                        kipList.set(i, kip);

                                    }


                                }
                            }
                        }
                    }


                    Collections.sort(kipList, new Comparator<Kip>() {
                        @Override
                        public int compare(Kip kip, Kip t1) {
                            return Float.parseFloat(kip.getKmToObj()) < Float.parseFloat(t1.getKmToObj()) ? -1 : (Float.parseFloat(kip.getKmToObj()) > Float.parseFloat(t1.getKmToObj())) ? 1 : 0;
                        }
                    });


                } catch (Exception e) {

                    onResponseFailure(e);


                }


            }
        });

        return kipList;

    }
