##EXAMPLE:

##$\text{ANOTHER ONE BITES THE DUST}$

###$$\text{ANOTHER ONE BITES THE DUST}$$

#$$\text{ANOTHER ONE BITES THE DUST}$$
Input lists:

$$Ranges = [(0,5),(-3,2),(7,10),(-8,3),(12,15),(5,9),(2,11),(-5,6),(8,13)]$$

$$points = [5]$$

**Objective**: Find how many ranges includes the number 5
___

**1)** Sort the range list in ascending order using the inferior limit. (MergeSort)

$$ranges = [(-8,3),(-5,6),(-3,2),(0,5),(2,11),(5,9),(7,10),(8,13),(12,15)]$$

___

**2)** Find the index of the range where the inferior limit is higher the point being searched.(Binary Search)

$$Index = 6$$

Because the range in index 6 is $(7,10)$. From that possition and forward, all ranges won't contain the number 5, so we can discard that list.

___

**3)** Slice the list in that index, so we are left with the list that all the inferior limits are lower or equal than the point being searched.

$$ranges = [(-8,3),(-5,6),(-3,2),(0,5),(2,11),(5,9)]$$

___
**4)** Sorting the range list in ascending order acording to the Superior limit.(MergeSort)

$$ranges = [(-3,2),(-8,3),(0,5),(-5,6),(5,9),(2,11)]$$

___
**5)** Find the index of the lowest range that its superior limit is equal or greater than the point that is being searched.(Binary Search)

$$Index = 2$$

Because the range in index 2 is $(0,5)$ and is the first range in the sorted list that contains the point 5. Forward from that index to the rest of the list we will find all ranges includes the number 5.

___
**6)** Slice the list in the index found, leaving with the list of ranges that contains the point being searched.

$$ranges = [(0,5),(-5,6),(5,9),(2,11)]$$

___

**7)**The returning number is the length of that final resulting list.

$$Result = 4$$

Since there are 4 ranges that contains the number 5