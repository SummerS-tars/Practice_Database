# Physical Storage System and Data Storage Structure

## 1. Physical Storage System

type:  

- volatile storage(易失性存储)  
- non-volatile storage(非易失性存储)  

factors influencing choice:  

- speed  
- cost
- reliability  

### 1.1. Storage Hierarchy(存储层次结构)

- primary storage(主存储)  
    fast bug volatile  
    - cache
    - main memory  
- secondary storage(辅助存储),also online storage  
    - flash memory  
    - magnetic disk  
- tertiary storage(三级存储),also offline storage  

### 1.2. Disk Subsystem(磁盘子系统)

Disk controller: interfaces between the computer system and teh disk drive hardware  

it will do many things  

Storage Interfaces standards:  

- SATA(Serial ATA, 串行ATA)  
    *ATA: Advanced Technology Attachment(高级技术附件)*  
- SAS(Serial Attached SCSI, 串行连接SCSI)  
    *SCSI(Small Computer System Interface, 小型计算机系统接口)*  
- NVMe(Non-Volatile Memory Express, 非易失性内存快速存储)  

Storage Area Network(SAN, 存储区域网络)  

Network Attached Storage(NAS, 网络附加存储): provides a file system interface using networked file system protocol, instead of providing a disk system interface  

## 2. Magnetic Disk(磁盘)

components:  

- read-write head
- tracks(磁道)  
    surface of platter --> circular tracks  
    about 50k-100k tracks per surface  
- sectors(扇区)  
    smallest unit of data that can be read or written  
    typically 512 bytes per sector  
    larger track has more sectors  
    typically 500(inner) to 1000(outer) sectors per track  

### 2.1. Performance Measures of Disks

- Access time  
    - seek time(寻道时间)  
    - rotational latency(旋转延迟)
- Data-transfer rate(数据传输速率)

- sequential access pattern(顺序访问模式)  
- random access pattern(随机访问模式)  
- I/O operation per second(每秒I/O操作数)  

- MMTF(Mean Time To Failure, 平均故障时间)  

### 2.2. Optimization of Disk-Block Access

**Disk block**: unit for storage allocation and retrieval  

data is transferred between main memory and disk in blocks  

**block size**  
the block can be smaller or larger  
they have different advantages and disadvantages  
typically 4-16KB  

**Disk-arm-scheduling**  
all in all, it is to minimize the movement times of the arm  
like elevator algorithm  

**file organization**  
let data be stored closer  
defragmented(去碎片化, 碎片整理)  

**Nonvolatile write buffer**  

### 2.3. Flash Storage

NAND flash  

Solid State Disk(SSD, 固态硬盘)  

## 3. RAID

RAID(Redundant Array of Independent Disks, 独立冗余磁盘阵列)  

disk organization techniques  
manage a large numbers of disks  
providing a view of a single disk that is  
**high capacity** and **high speed**  
and **high reliability**  

### 3.1. Advantages

Improvement in Performance via Parallelism  
two main goals:  

1. load balancing(负载均衡)  
2. reduce response time  

Striping data across multiple disks  
utilizing all disks in parallel  

- **Bit-level striping**  
    now not used anymore
- **Block-level striping**  

### 3.2. RAID Levels

common levels:  

- RAID 0  
    block striping; non-redundant  
- RAID 1  
    mirrored disks with block striping  
    known as 1 + 0  
    the best write performance  
- RAID 5  
    - Block-Interleaved Distributed Parity(块交错分布奇偶校验)  
        the parity block is distributed across all the disks  
        to avoid single parity disk to be the bottleneck  

**Parity Blocks(奇偶校验块)**  
the XOR result of all the corresponding data blocks in all the disks  
if one of the disk fails, the data in it can be recovered from the result of the XOR of the other disks and the parity block  

1 xor 1 = 0 and 0 xor 1 = 1  

How to choose?  

data can be easily recovered from other sources and need high performance:  
RAID 0  

low update rate and large amount of data:  
RAID 5  

less requirements for space efficiency:  
RAID 1  

## 4. File Organization(文件组织)

the database is stored as a collection of files  
each is a sequence of records  
a record is a sequence of fields  

then we'll wonder how the files are organized?  

assume one approach:  

- record size fixed
- each file one particular type only  
- different files for different relations  

### 4.1. Fixed-Length Records

n bytes per record  
the `i`th record starts from byte `i * n`  

accessing is simple  
but record may across blocks  

#### 4.1.1. Deletion

delete the `i`th record  

- move the following records 1 position forward  

- move the last record to the position of the deleted record(`i`th)  

- just leave it empty and keep a free list to track the empty records  

**free list**  
similar to linked list  
reuse the space of the deleted record to store pointers  

### 4.2. Variable-Length Records

two ways:

- multiple records type in a file
- the types allow variable len for some fields  

#### Record Structure

1. (offset, length) pairs array  
2. fixed-length fields(if any)  
3. null bitmap  
4. variable-length fields  

#### Slotted Page Structure(槽页结构)

**Attention:**  
this concept is for the block level  
not for the record level  

including:  

- Block header  
    - number of records in the header  
    - the tail of the free space  
    - an array of slots  
- slot array  
    each slot stores the position and size of a record  

*using this structure, we can decouple the address of the records from the pointers*  
*the sequence of the slots has no relation to the sequence of the records*  

### Organization of Records in Files

- **Heap**  
- **Sequential**  
- **Multi-table clustering file organization**  
- **B+-tree file organization**  
- **Hashing**  

#### Heap

a record can be placed anywhere in the file  
where there is space  

#### Sequential

store records in sequential order  
based on the value of the search key of each record  

- [ ] what is the search key?  

#### Multi-table Clustering File Organization

records of several different relations can be stored in the same file  

why?  
minimize I/O  

#### B+-tree File Organization

ordered storage even with inserts/deletes  

#### Hashing

a hash function computed on search key  
the result specified in which block of the file the record should be placed  

## 5. Data-Dictionary Storage(数据字典存储)

**Data Dictionary**(also called **System Catalog**)  
stores metadata  
*(data about data)*  

e.g. info about:  

- relations  
- indices
- physical file organization info  

## 6. Storage Structure(存储结构)
