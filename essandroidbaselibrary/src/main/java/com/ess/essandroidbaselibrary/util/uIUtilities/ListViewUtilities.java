package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 6/11/2018.
 */

public class ListViewUtilities
{
        /**
         * Sets ListView height dynamically based on the height of the items.
         *
         * @param listView to be resized
         * @param pixelOffsetPerItem to be handle extra space. 0 - no space handling, minus for deduct, positive for add offset
         * @return true if the listView is successfully resized, false otherwise
         */
        public static boolean setListViewHeightBasedOnItems(ListView listView, int pixelOffsetPerItem)
        {

            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter != null) {

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, listView);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = listView.getDividerHeight() *
                        (numberOfItems - 1);

                // Offset to handle extra space
                int totalOffset = pixelOffsetPerItem * numberOfItems;

                // Set list height.
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight + totalOffset;
                listView.setLayoutParams(params);
                listView.requestLayout();

                return true;

            } else {
                return false;
            }

        }
}
